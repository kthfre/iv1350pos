package se.kth.iv1350.registersystem.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kth.iv1350.registersystem.dbhandler.SaleDTO;

import static org.junit.Assert.*;

public class ControllerTest {
    private Controller ctrl;
    private SaleDTO item;

    @Before
    public void setUp() {
        this.ctrl = new Controller();
        ctrl.initiateSale();
    }

    @After
    public void tearDown() {
        this.ctrl = null;
        this.item = null;
    }

    @Test
    public void testInvalidAndZeroItemsInitiateSale() {
        ctrl.initiateSale();
        item = ctrl.scanItem(0, "abc123");
        assertEquals(item.getRunningTotal(), 0, 0.01);
    }

    @Test
    public void testDescriptionOneExistingScanItem() {
        item = ctrl.scanItem(1, "123");
        assertEquals("Description not matching.", "Great book!", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceOneExistingScanItem() {
        item = ctrl.scanItem(1, "123");
        assertEquals(21.00 * 1.06, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionMultipleExistingScanItem() {
        item = ctrl.scanItem(5, "321");
        assertEquals("Description not matching.", "Tomato soup", item.getDescription());
        assertEquals(39.99 * 1.12, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testPriceMultipleExistingScanItem() {
        item = ctrl.scanItem(5, "321");
        assertEquals(39.99 * 1.12, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionOneRightFormatNonExistingScanItem() {
        item = ctrl.scanItem(1, "999");
        assertEquals("Description not matching.", "Invalid item identifier or quantity.", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceOneRightFormatNonExistingScanItem() {
        item = ctrl.scanItem(1, "999");
        assertEquals(0, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionOneWrongFormatNonExistingScanItem() {
        item = ctrl.scanItem(1, "Johan");
        assertEquals("Description not matching.", "Invalid item identifier or quantity.", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceOneWrongFormatNonExistingScanItem() {
        item = ctrl.scanItem(1, "Johan");
        assertEquals(0, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionMultipleRightFormatNonExistingScanItem() {
        item = ctrl.scanItem(9, "999");
        assertEquals("Description not matching.", "Invalid item identifier or quantity.", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceMultipleRightFormatNonExistingScanItem() {
        item = ctrl.scanItem(9, "999");
        assertEquals(0, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionMultipleWrongFormatNonExistingScanItem() {
        item = ctrl.scanItem(7, "Johanna39");
        assertEquals("Description not matching.", "Invalid item identifier or quantity.", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceMultipleWrongFormatNonExistingScanItem() {
        item = ctrl.scanItem(7, "Johanna39");
        assertEquals(0, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionEmptyIdentifierScanItem() {
        item = ctrl.scanItem(1, "");
        assertEquals("Description not matching.", "Invalid item identifier or quantity.", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceEmptyIdentifierScanItem() {
        item = ctrl.scanItem(1, "");
        assertEquals(0, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionZeroItemsExistingScanItem() {
        item = ctrl.scanItem(0, "123");
        assertEquals("Description not matching.", "Invalid item identifier or quantity.", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceZeroItemsExistingScanItem() {
        item = ctrl.scanItem(0, "123");
        assertEquals(0, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionZeroItemsNonExistingScanItem() {
        item = ctrl.scanItem(0, "999");
        assertEquals("Description not matching.", "Invalid item identifier or quantity.", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceZeroItemsNonExistingScanItem() {
        item = ctrl.scanItem(0, "999");
        assertEquals(0, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testRunningTotalSingleItemDiscountExistingCustomerApplyDiscount() {
        double total = 39.99 * 1.12 + 21.00 * 1.06 + 4.99 * 1.25;
        ctrl.scanItem(1, "321");
        ctrl.scanItem(1, "123");
        ctrl.scanItem(1, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testDiscountedTotalSingleItemDiscountExistingCustomerApplyDiscount() {
        double total = ((39.99 * 1.12) * 0.90) + 21.00 * 1.06 + 4.99 * 1.25;
        ctrl.scanItem(1, "321");
        ctrl.scanItem(1, "123");
        ctrl.scanItem(1, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testDiscountTypeSingleItemDiscountExistingCustomerApplyDiscount() {
        ctrl.scanItem(1, "321");
        ctrl.scanItem(1, "123");
        ctrl.scanItem(1, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(1, item.getDiscountType());
    }

    @Test
    public void testRunningTotalNumberOfItemsDiscountExistingCustomerApplyDiscount() {
        double total = 1 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 6 * 4.99 * 1.25;
        ctrl.scanItem(1, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(6, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testDiscountedTotalNumberOfItemsDiscountExistingCustomerApplyDiscount() {
        double total = (1 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 6 * 4.99 * 1.25) * 0.85;
        ctrl.scanItem(1, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(6, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testDiscountTypeNumberOfItemsDiscountExistingCustomerApplyDiscount() {
        ctrl.scanItem(1, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(6, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(2, item.getDiscountType());
    }

    @Test
    public void testRunningTotalTotalAmountDiscountExistingCustomerApplyDiscount() {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testDiscountedTotalTotalAmountDiscountExistingCustomerApplyDiscount() {
        double total = (3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25) * 0.80;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testDiscountTypeTotalAmountDiscountExistingCustomerApplyDiscount() {
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(3, item.getDiscountType());
    }

    @Test
    public void testDiscountedTotalTotalAmountDiscountDetermineSuperiorAvaiableDiscountExistingCustomerApplyDiscount() {
        double total = (5 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25) * 0.70;
        ctrl.scanItem(5, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testThresholdNotMetExistingCustomerApplyDiscount() {
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(-1, item.getDiscountType());
    }

    @Test
    public void testNonExistingCustomerApplyDiscount() {
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0505050505");
        assertEquals(0, item.getDiscountType());
    }

    @Test
    public void testSufficientAmountPay() {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.pay(250.00);
        assertEquals(250 - total, item.getChange(), 0.01);
    }

    @Test
    public void testInsufficientAmountPay() {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.pay(225.00);
        assertEquals(225 - total, item.getChange(), 0.01);
    }

    @Test
    public void testNoAmountPay() {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.pay(0);
        assertEquals(-total, item.getChange(), 0.01);
    }

    @Test
    public void testDiscountTooLowAmountThenQuitPay() {
        double total = 3 * 21 * 1.06 * 0.90;
        ctrl.scanItem(3, "123");
        ctrl.applyDiscount("0101010101");
        item = ctrl.pay(5);
        assertEquals(-total + 5, item.getChange(), 0.01);
    }

    @Test
    public void testExistingRetrieveLog() {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        ctrl.pay(250);

        ctrl.initiateSale();
        ctrl.scanItem(3, "321");
        ctrl.pay(155);

        ctrl.initiateSale();
        ctrl.scanItem(10, "222");
        ctrl.pay(50);

        ctrl.initiateSale();
        ctrl.scanItem(10, "222");
        ctrl.pay(75);

        item = ctrl.retrieveLog(2);
        assertEquals(250 - total, item.getChange(), 0.01);
    }

    @Test
    public void testNonExistingRetrieveLog() {
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        ctrl.pay(250);

        ctrl.initiateSale();
        ctrl.scanItem(3, "321");
        ctrl.pay(155);

        ctrl.initiateSale();
        ctrl.scanItem(10, "222");
        ctrl.pay(50);

        ctrl.initiateSale();
        ctrl.scanItem(10, "222");
        ctrl.pay(75);

        item = ctrl.retrieveLog(3);
        assertNull("Attempted retrieval of non-existing log has an unexpected return value", item);
    }

    @Test
    public void testNegativeIndexRetrieveLog() {
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        ctrl.pay(250);

        ctrl.initiateSale();
        ctrl.scanItem(3, "321");
        ctrl.pay(155);

        ctrl.initiateSale();
        ctrl.scanItem(10, "222");
        ctrl.pay(50);

        ctrl.initiateSale();
        ctrl.scanItem(10, "222");
        ctrl.pay(75);

        item = ctrl.retrieveLog(-1);
        assertNull("Attempted retrieval of non-existing log has an unexpected (not null) return value", item);
    }

    @Test
    public void testRetrieveLogCount() {
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        ctrl.pay(250);

        ctrl.initiateSale();
        ctrl.scanItem(3, "321");
        ctrl.pay(155);

        ctrl.initiateSale();
        ctrl.scanItem(10, "222");
        ctrl.pay(50);

        ctrl.initiateSale();
        ctrl.scanItem(10, "222");
        ctrl.pay(75);

        assertEquals(3, ctrl.retrieveLogCount());
    }

    @Test
    public void testMatchingStringsInterpretInput() {
        String testString = "testString321";
        assertTrue("Unexpected return value (false) despite equal strings.", ctrl.interpretInput("testString321", testString));
    }

    @Test
    public void testNonMatchingStringsInterpretInput() {
        String testString = "testString4321";
        assertFalse("Unexpected return value (false) despite equal strings.", ctrl.interpretInput("testString321", testString));
    }
}