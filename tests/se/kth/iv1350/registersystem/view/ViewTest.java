package se.kth.iv1350.registersystem.view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kth.iv1350.registersystem.controller.Controller;
import se.kth.iv1350.registersystem.controller.FatalException;
import se.kth.iv1350.registersystem.controller.GeneralFailureException;
import se.kth.iv1350.registersystem.dbhandler.SaleDTO;

import java.util.Scanner;

import static org.junit.Assert.*;

public class ViewTest {
    private View view;
    private Controller ctrl;
    private SaleDTO item;

    @Before
    public void setUp() {
        this.ctrl = new Controller();
        this.view = new View(ctrl);
    }

    @After
    public void tearDown() {
        this.ctrl = null;
        this.view = null;
        this.item = null;
    }

    @Test
    public void testWithNoItemsQuitSaleCreatedSimulateSale() throws GeneralFailureException {
        view.simulateSale(new Scanner("done" +
                "\nquit"));

        try {
            item = ctrl.scanItem(0, null);
            fail("No exception thrown despite 0 quantity and invalid identifier.");
        } catch (FatalException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testWithNoItemsQuitWithRandomNoiseSaleCreatedSimulateSale() throws GeneralFailureException {
        view.simulateSale(new Scanner("done" +
                "\nrandom" +
                "\nstuff" +
                "\nqui" +
                "\n sal" +
                "\n1238302" +
                "\n123;3" +
                "\nquit"));

        try {
            item = ctrl.scanItem(0, null);
            fail("No exception thrown despite 0 quantity.");
        } catch (FatalException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testRealItemIdentifierInvalidQuantifierSimulateSale() throws GeneralFailureException {
        view.simulateSale(new Scanner("123;-3" +
                "\ndone" +
                "\nq"));

        try {
            item = ctrl.scanItem(0, null);
            fail("No exception thrown despite 0 quantity.");
        } catch (FatalException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testRealItemIdentifierInvalidQuantifierThenValidNoDiscountThenPayAndQuitSimulateSale() {
        view.simulateSale(new Scanner("123;-3" +
                "\n123;3" +
                "\ndone" +
                "\ndone" +
                "\n67" +
                "\nquit"));
        assertEquals(66.78, ctrl.retrieveLog(0).getRunningTotal(), 0.01);
    }

    @Test
    public void testMixOfValidAndInvalidNoDiscountThenPayAndQuitSimulateSale() {
        view.simulateSale(new Scanner("123;-3" +
                "\n123;3" +
                "\n3982183" +
                "\n321;5" +
                "\n321;0" +
                "\ndone" +
                "\ndone" +
                "\n300" +
                "\nquit"));
        assertEquals(300 - (3 * 21 * 1.06 + 5* 39.99 * 1.12), ctrl.retrieveLog(0).getChange(), 0.01);
    }

    @Test
    public void testTooLowPaymentDiscountThenQuitSimulateSale() {
        view.simulateSale(new Scanner("123" +
                "\n123;2" +
                "\n3982183" +
                "\ndone" +
                "\ngubben79" +
                "\n0101010101" +
                "\n5" +
                "\nquit" +
                "\nquit"));
        assertEquals(0, ctrl.retrieveLogCount());
    }

    @Test
    public void testMixOfThreeConsecutiveSalesRetrieveLogsThenQuitSimulateSale() {
        view.simulateSale(new Scanner("123;-3" +
                "\n123;3" +
                "\n3982183" +
                "\n321;5" +
                "\n321;0" +
                "\ndone" +
                "\ndone" +
                "\n35bananer" +
                "\n300"+
                "\n quit  "));

        view.simulateSale(new Scanner("321;2" +
                "\n111" +
                "\n3982183" +
                "\nHejHopp¤=#\"Sp\\\\" +
                "\n111;0" +
                "\n333" +
                "\n333;3" +
                "\n123;9" +
                "\ndone" +
                "\n0303030303" +
                "\n335" +
                "\n quit"));

        view.simulateSale(new Scanner("321;5" +
                "\n321;0" +
                "\ndone" +
                "\n0505050505" +
                "\ndone" +
                "\n250" +
                "\nlogs"));
        assertEquals(3, ctrl.retrieveLogCount());
    }

    @Test
    public void testMixOfThreeConsecutiveSalesCorrectChangeOnRetrievedLogThenQuitSimulateSale() {
        double change = 335 - ((39.99 * 1.12 * 2 + 149 * 1.25 + 21 * 1.06 * 9) * 0.70);
        view.simulateSale(new Scanner("123;-3" +
                "\n123;3" +
                "\n3982183" +
                "\n321;5" +
                "\n321;0" +
                "\ndone" +
                "\ndone" +
                "\n300"+
                "\n quit  "));

        view.simulateSale(new Scanner("321;2" +
                "\n111" +
                "\n3982183" +
                "\nHejHopp¤=#\"Sp\\\\" +
                "\n111;0" +
                "\n333" +
                "\n333;3" +
                "\n123;9" +
                "\ndone" +
                "\n0303030303" +
                "\n335" +
                "\n quit"));

        view.simulateSale(new Scanner("321;5" +
                "\n321;0" +
                "\ndone" +
                "\n0505050505" +
                "\ndone" +
                "\n250" +
                "\nlogs"));
        assertEquals(change, ctrl.retrieveLog(1).getChange(), 0.01);
    }

    @Test
    public void testRandomNoiseAndInvalidInputsAtEveryStageSimulateSale() {
        double change = 500.5 - ((149 * 1.25 * 2) * 0.70);
        view.simulateSale(new Scanner("wanttobuy25rockets" +
                "\nnf4380nf0sfdnbf43bf49" +
                "\n1111" +
                "\nHejHopp¤=#\"Sp\\\\" +
                "\n111;0" +
                "\n111;-1" +
                "\n1111;2" +
                "\n111" +
                "\n111;1" +
                "\n333;1" +
                "\n333;0" +
                "\n333" +
                "\ndone" +
                "\nborntobeyourbaby\\\\bonjovi" +
                "\n010101-0101" +
                "\n20010101-0101" +
                "\n200101010101" +
                "\nnjgdfg+43ng034" +
                "\n0505050505" +
                "\n0303030303" +
                "\n20bucketsofwater" +
                "\ngnfdg08TTT&&&%¤/\"dfgGFDMg-df.m35" +
                "\n123;9" +
                "\n500.500" +
                "\n-5000" +
                "\n500.50" +
                "\n     logs             "));

        assertEquals(change, ctrl.retrieveLog(0).getChange(), 0.01);
    }
}