package se.kth.iv1350.registersystem.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kth.iv1350.registersystem.dbhandler.CustomerDiscountDTO;
import se.kth.iv1350.registersystem.dbhandler.InventoryDTO;
import se.kth.iv1350.registersystem.dbhandler.SaleDTO;

import static org.junit.Assert.*;

public class RegisterTest {
    private Register register;
    private SaleDTO item;
    private CustomerDiscountDTO[] discounts;

    @Before
    public void setUp() {
        this.register = new Register();
    }

    @After
    public void tearDown() {
        this.register = null;
        this.item = null;
        this.discounts = null;
    }

    @Test
    public void testNotInitiatedInitiateSale() {
        register.logSale();
        assertNull("Sale instantiated despite new sale not being initiated.", register.retrieveLog(0));
    }

    @Test
    public void testInitiateSale() {
        register.initiateSale();
        item = register.registerItem(1, null);
        assertEquals(0, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testValidQuantityExistingItemRegisterItem() {
        register.initiateSale();
        item = register.registerItem(2, new InventoryDTO(21.00, 6.00, "123", "Great book!"));
        assertEquals(44.52, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testNegativeQuantityNonExistingItemRegisterItem() {
        register.initiateSale();
        item = register.registerItem(-999, null);
        assertEquals("Invalid item identifier or quantity.", item.getDescription());
    }

    @Test
    public void testSelectsMostFavorableDiscountPerItemDiscountApplyDiscount() {
        discounts = new CustomerDiscountDTO[] {new CustomerDiscountDTO("0202020202", 1, 123, 11),
                new CustomerDiscountDTO("0202020202", 2, 10, 10), new CustomerDiscountDTO("0202020202", 3, 150, 10)};
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(1, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        item = register.applyDiscount(discounts);
        assertEquals(1, item.getDiscountType());
    }

    @Test
    public void testSelectsMostFavorableDiscountNumberOfItemsDiscountApplyDiscount() {
        discounts = new CustomerDiscountDTO[] {new CustomerDiscountDTO("0202020202", 1, 123, 12),
                new CustomerDiscountDTO("0202020202", 2, 10, 11), new CustomerDiscountDTO("0202020202", 3, 150, 10)};
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        item = register.applyDiscount(discounts);
        assertEquals(2, item.getDiscountType());
    }

    @Test
    public void testSelectsMostFavorableDiscountTotalAmountDiscountApplyDiscount() {
        discounts = new CustomerDiscountDTO[] {new CustomerDiscountDTO("0202020202", 1, 123, 12),
                new CustomerDiscountDTO("0202020202", 2, 10, 10), new CustomerDiscountDTO("0202020202", 3, 150 ,11)};
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        item = register.applyDiscount(discounts);
        assertEquals(3, item.getDiscountType());
    }

    @Test
    public void testWithoutDiscountSubmitPayment() {
        double total = 21 * 1.06 * 9 + 4.99 * 1.25 * 3;
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        item = register.submitPayment(220);
        assertEquals(220 - total, item.getChange(), 0.01);
    }

    @Test
    public void testWithDiscountSubmitPayment() {
        double total = (21 * 1.06 * 9 + 4.99 * 1.25 * 3) * 0.89;
        discounts = new CustomerDiscountDTO[] {new CustomerDiscountDTO("0202020202", 1, 123, 12),
                new CustomerDiscountDTO("0202020202", 2, 10, 10), new CustomerDiscountDTO("0202020202", 3, 150, 11)};
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.applyDiscount(discounts);
        item = register.submitPayment(220);
        assertEquals(220 - total, item.getChange(), 0.01);
    }

    @Test
    public void testWithoutSufficientPaymentLogSale() {
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(100);
        assertNull("Sale has been logged despite no payment.", register.retrieveLog(0));
    }

    @Test
    public void testWithNoInitiatedSaleLogSale() {
        register.logSale();
        assertNull("Sale loggable despite not having been initiated.", register.retrieveLog(0));
    }

    @Test
    public void testSecondLastSuccessfulPurchaceRetrieveLog() {
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(300);

        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(10);

        register.initiateSale();
        register.registerItem(5, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(5, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(200);
        assertEquals(300, register.retrieveLog(1).getPaid(), 0.01);
    }

    @Test
    public void testOneFailedPaymentRetrieveLogCount() {
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(300);

        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(10);

        register.initiateSale();
        register.registerItem(5, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(5, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(200);
        assertEquals(2, register.retrieveLogCount());
    }

    @Test
    public void testWithOneFailedPaymentGetBalance() {
        double balance = 9 * 21 * 1.06 + 3 * 4.99 * 1.25 + 5 * 21 * 1.06 + 5 * 4.99 * 1.25;
        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(300);

        register.initiateSale();
        register.registerItem(9, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(3, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(10);

        register.initiateSale();
        register.registerItem(5, new InventoryDTO(21.00, 6.0, "123", "Great book!"));
        register.registerItem(5, new InventoryDTO(4.99, 25.0, "222", "Premium pen"));
        register.submitPayment(200);
        assertEquals(balance, register.getBalance(), 0.01);
    }

    @Test
    public void testMatchingInterpretInput() {
        String pattern = "(?i)^teststring[1-4]+$";
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("testString44332211223344", pattern));
    }

    @Test
    public void testNonMatchingInterpretInput() {
        String pattern = "(?i)^teststring[1-4]+$";
        assertFalse("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("testString443322112233449", pattern));
    }

    @Test
    public void testAllValidPatternsInterpretInput() {
        String isDone = "(?i)^\\s*done\\s*$";
        String showLogs = "(?i)^\\s*(show|logs)\\s*$";
        String nextSale = "(?i)^\\s*(next|sale)\\s*$";
        String isItemIdentifier = "^\\s*([0-9]{3}(;[0-9]{1,3})?)\\s*$";
        String isQuit = "(?i)^\\s*(q|quit)\\s*$";
        String isIdentification = "^\\s*(0[1-9]|[1-9][0-9])(0[1-9]|1[0-2])(0[1-9]|[1-2][1-9]|3[0-1])[0-9]{4}\\s*$";
        String isPayment = "\\s*[0-9]+(\\.[0-9]{0,2})?\\s*";

        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("done", isDone));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("  done", isDone));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("done ", isDone));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("show", showLogs));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("show     ", showLogs));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput(" show", showLogs));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("logs", showLogs));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("   logs", showLogs));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("logs    ", showLogs));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("next", nextSale));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput(" next", nextSale));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("next   ", nextSale));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("sale", nextSale));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("      sale", nextSale));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("sale\t", nextSale));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("123", isItemIdentifier));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput(" 123 ", isItemIdentifier));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("123;3", isItemIdentifier));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("123;333  ", isItemIdentifier));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput(" q\t", isQuit));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("\t q\t", isQuit));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput(" quit\t\t", isQuit));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("quit", isQuit));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("0101010101", isIdentification));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("0505050505   ", isIdentification));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("0512010505   ", isIdentification));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("0502290505   ", isIdentification));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("0503310505   ", isIdentification));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("250", isPayment));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("250000", isPayment));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("250.", isPayment));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("250. ", isPayment));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput(" \t250.1", isPayment));
        assertTrue("Unexpected return value (false) despite string matching regex pattern.", register.interpretInput("   250.55  ", isPayment));
    }

    @Test
    public void testAllInvalidPatternsInterpretInput() {
        String isDone = "(?i)^\\s*done\\s*$";
        String showLogs = "(?i)^\\s*(show|logs)\\s*$";
        String nextSale = "(?i)^\\s*(next|sale)\\s*$";
        String isItemIdentifier = "^\\s*([0-9]{3}(;[0-9]{1,3})?)\\s*$";
        String isQuit = "(?i)^\\s*(q|quit)\\s*$";
        String isIdentification = "^\\s*(0[1-9]|[1-9][0-9])(0[1-9]|1[0-2])(0[1-9]|[1-2][1-9]|3[0-1])[0-9]{4}\\s*$";
        String isPayment = "\\s*[0-9]+(\\.[0-9]{0,2})?\\s*";

        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("tdone;", isDone));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("am done;", isDone));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("done now", isDone));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("donee;", isDone));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("showlogs", showLogs));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("showw", showLogs));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("show;", showLogs));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("show logs", showLogs));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput(" logs logs", showLogs));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("next;", nextSale));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("sale;", nextSale));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("salenext;", nextSale));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("123;", isItemIdentifier));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput(";", isItemIdentifier));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput(";3", isItemIdentifier));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("1234;4", isItemIdentifier));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("1234", isItemIdentifier));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("3;123", isItemIdentifier));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("123;;3", isItemIdentifier));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("123:3", isItemIdentifier));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("q q", isQuit));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("q quit", isQuit));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("qquit", isQuit));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("uit", isQuit));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("qu", isQuit));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("qui", isQuit));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("18800101010101", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("010101-0101", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("19010101-0101", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("010101", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("19010101", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("0513010505", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("0012010505", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("0512000505", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("0512320505", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("0500010505", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("0521010505", isIdentification));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("250.550", isPayment));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("250.5500", isPayment));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("250,", isPayment));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("250,5", isPayment));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("250,55", isPayment));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput("250..55", isPayment));
        assertFalse("Unexpected return value (true) despite string matching regex pattern.", register.interpretInput(".55", isPayment));
    }


}