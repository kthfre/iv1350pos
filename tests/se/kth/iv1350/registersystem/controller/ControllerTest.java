package se.kth.iv1350.registersystem.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kth.iv1350.registersystem.dbhandler.DataRetrievalException;
import se.kth.iv1350.registersystem.dbhandler.InventoryException;
import se.kth.iv1350.registersystem.dbhandler.SaleDTO;
import se.kth.iv1350.registersystem.model.ItemQuantityException;

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

        try {
            item = ctrl.scanItem(0, "abc123");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testDescriptionOneExistingScanItem() throws GeneralFailureException {
        item = ctrl.scanItem(1, "123");
        assertEquals("Description not matching.", "Great book!", item.getDescription());
        // total assertEquals()
    }

    @Test
    public void testPriceOneExistingScanItem() throws GeneralFailureException {
        item = ctrl.scanItem(1, "123");
        assertEquals(21.00 * 1.06, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testDescriptionMultipleExistingScanItem() throws GeneralFailureException {
        item = ctrl.scanItem(5, "321");
        assertEquals("Description not matching.", "Tomato soup", item.getDescription());
        assertEquals(39.99 * 1.12, item.getPrice(), 0.01);
        // total assertEquals()
    }

    @Test
    public void testPriceMultipleExistingScanItem() throws GeneralFailureException {
        item = ctrl.scanItem(5, "321");
        assertEquals(39.99 * 1.12, item.getPrice(), 0.01);
    }

    @Test
    public void testDescriptionOneRightFormatNonExistingScanItem() {
        try {
            item = ctrl.scanItem(1, "999");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testPriceOneRightFormatNonExistingScanItem() {
        try {
            item = ctrl.scanItem(1, "999");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testDescriptionOneWrongFormatNonExistingScanItem() {
        try {
            item = ctrl.scanItem(1, "Johan");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testPriceOneWrongFormatNonExistingScanItem() {
        try {
            item = ctrl.scanItem(1, "Johan");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testDescriptionMultipleRightFormatNonExistingScanItem() {
        try {
            item = ctrl.scanItem(9, "999");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testPriceMultipleRightFormatNonExistingScanItem() {
        try {
            item = ctrl.scanItem(9, "999");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testDescriptionMultipleWrongFormatNonExistingScanItem() {
        try {
            item = ctrl.scanItem(7, "Johanna39");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testPriceMultipleWrongFormatNonExistingScanItem() {
        try {
            item = ctrl.scanItem(7, "Johanna39");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testDescriptionEmptyIdentifierScanItem() {
        try {
            item = ctrl.scanItem(1, "");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testPriceEmptyIdentifierScanItem() {
        try {
            item = ctrl.scanItem(1, "");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testDescriptionZeroItemsExistingScanItem() {
        try {
            item = ctrl.scanItem(0, "123");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "faulty quantity");
        }
    }

    @Test
    public void testPriceZeroItemsExistingScanItem() {
        try {
            item = ctrl.scanItem(0, "123");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "faulty quantity");
        }
    }

    @Test
    public void testDescriptionZeroItemsNonExistingScanItem() {
        try {
            item = ctrl.scanItem(0, "999");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testPriceZeroItemsNonExistingScanItem() {
        try {
            item = ctrl.scanItem(0, "999");
            fail("No exception thrown despite invalid identifier.");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testRunningTotalSingleItemDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        double total = 39.99 * 1.12 + 21.00 * 1.06 + 4.99 * 1.25;
        ctrl.scanItem(1, "321");
        ctrl.scanItem(1, "123");
        ctrl.scanItem(1, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testDiscountedTotalSingleItemDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        double total = ((39.99 * 1.12) * 0.90) + 21.00 * 1.06 + 4.99 * 1.25;
        ctrl.scanItem(1, "321");
        ctrl.scanItem(1, "123");
        ctrl.scanItem(1, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testDiscountTypeSingleItemDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        ctrl.scanItem(1, "321");
        ctrl.scanItem(1, "123");
        ctrl.scanItem(1, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(1, item.getDiscountType());
    }

    @Test
    public void testRunningTotalNumberOfItemsDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        double total = 1 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 6 * 4.99 * 1.25;
        ctrl.scanItem(1, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(6, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testDiscountedTotalNumberOfItemsDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        double total = (1 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 6 * 4.99 * 1.25) * 0.85;
        ctrl.scanItem(1, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(6, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testDiscountTypeNumberOfItemsDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        ctrl.scanItem(1, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(6, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(2, item.getDiscountType());
    }

    @Test
    public void testRunningTotalTotalAmountDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testDiscountedTotalTotalAmountDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        double total = (3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25) * 0.80;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testDiscountTypeTotalAmountDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(3, item.getDiscountType());
    }

    @Test
    public void testDiscountedTotalTotalAmountDiscountDetermineSuperiorAvaiableDiscountExistingCustomerApplyDiscount() throws GeneralFailureException {
        double total = (5 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25) * 0.70;
        ctrl.scanItem(5, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(total, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testThresholdNotMetExistingCustomerApplyDiscount() throws GeneralFailureException {
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0303030303");
        assertEquals(-1, item.getDiscountType());
    }

    @Test
    public void testNonExistingCustomerApplyDiscount() throws GeneralFailureException {
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.applyDiscount("0505050505");
        assertEquals(0, item.getDiscountType());
    }

    @Test
    public void testSufficientAmountPay() throws GeneralFailureException {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.pay(250.00);
        assertEquals(250 - total, item.getChange(), 0.01);
    }

    @Test
    public void testInsufficientAmountPay() throws GeneralFailureException {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.pay(225.00);
        assertEquals(225 - total, item.getChange(), 0.01);
    }

    @Test
    public void testNoAmountPay() throws GeneralFailureException {
        double total = 3 * 39.99 * 1.12 + 3 * 21.00 * 1.06 + 4 * 4.99 * 1.25;
        ctrl.scanItem(3, "321");
        ctrl.scanItem(3, "123");
        ctrl.scanItem(4, "222");
        item = ctrl.pay(0);
        assertEquals(-total, item.getChange(), 0.01);
    }

    @Test
    public void testDiscountTooLowAmountThenQuitPay() throws GeneralFailureException {
        double total = 3 * 21 * 1.06 * 0.90;
        ctrl.scanItem(3, "123");
        ctrl.applyDiscount("0101010101");
        item = ctrl.pay(5);
        assertEquals(-total + 5, item.getChange(), 0.01);
    }

    @Test
    public void testExistingRetrieveLog() throws GeneralFailureException {
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
    public void testNonExistingRetrieveLog() throws GeneralFailureException {
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
    public void testNegativeIndexRetrieveLog() throws GeneralFailureException {
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
    public void testRetrieveLogCount() throws GeneralFailureException {
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

    @Test
    public void testSqlTimeoutInventoryQuery() throws GeneralFailureException {
        try {
            ctrl.scanItem(1,  "555");
            fail("Sql time out not causing appropriate exception");
        } catch (FatalException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testSqlNoTimeoutInventoryQuery() throws GeneralFailureException {
        try {
            item = ctrl.scanItem(1,  "123");
            assertEquals(item.getRunningTotal(), 21 * 1.06, 0.01);
        } catch (DataRetrievalException e) {
            fail("Sql time out exception thrown when no time out occured.");
        }
    }

    @Test
    public void testNotExistingInventoryQuery() {
        try {
            ctrl.scanItem(1,  "333");
            fail("Query for non-existing inventory not causing appropriate exception");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "item not found");
        }
    }

    @Test
    public void testExistingInventoryQuery() {
        try {
            item = ctrl.scanItem(1,  "123");
            assertEquals(item.getRunningTotal(), 21 * 1.06, 0.01);
        } catch (GeneralFailureException e) {
            fail("Exception wrongly thrown whehn query for existing inventory occurs.");
        }
    }

    @Test
    public void testFaultyQuantity() {
        try {
            ctrl.scanItem(0,  "123");
            fail("Attempting to register 0 quantity of an item not causing appropriate exception");
        } catch (GeneralFailureException e) {
            assertEquals(e.getMessage(), "faulty quantity");
        }
    }

    @Test
    public void testValidQuantity() {
        try {
            item = ctrl.scanItem(1,  "123");
            assertEquals(item.getRunningTotal(), 1.06 * 21, 0.01);
        } catch (GeneralFailureException e) {
            fail("Valid quantity causes exception to be wrongly thrown.");
        }
    }
}