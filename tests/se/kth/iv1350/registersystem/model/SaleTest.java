package se.kth.iv1350.registersystem.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kth.iv1350.registersystem.dbhandler.CustomerDiscountDTO;
import se.kth.iv1350.registersystem.dbhandler.InventoryDTO;
import se.kth.iv1350.registersystem.dbhandler.SaleDTO;

import static org.junit.Assert.*;

public class SaleTest {
    private Sale sale;
    private SaleDTO item;

    @Before
    public void setUp() {
        this.sale = new Sale();
    }

    @After
    public void tearDown() {
        this.sale = null;
        this.item = null;
    }

    @Test
    public void testExtremeQuantityRegisterItem() {
        item = sale.registerItem(1000000, new InventoryDTO(149.00, 25.0, "111", "Snow shovel"));
        assertEquals(1000000 * 149.0 * 1.25, item.getRunningTotal(), 0.01);
    }

    @Test
    public void testExtremeNegativeQuantityRegisterItem() {
        item = sale.registerItem(-1000000, new InventoryDTO(149.00, 25.0, "111", "Snow shovel"));
        assertEquals("Invalid item identifier or quantity.", item.getDescription());
    }

    @Test
    public void testOnePercentOnPerItemBasisApplyDiscount() {
        sale.registerItem(1000000, new InventoryDTO(149.00, 25.0, "111", "Snow shovel"));
        item = sale.applyDiscount(new CustomerDiscountDTO[] {new CustomerDiscountDTO("0101010101", 1, 111, 1)});
        assertEquals((1000000 * 149.0 * 1.25) * 0.99, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testElevenPercentOnNumberOfItemsBasisApplyDiscount() {
        sale.registerItem(1000000, new InventoryDTO(149.00, 25.0, "111", "Snow shovel"));
        item = sale.applyDiscount(new CustomerDiscountDTO[] {new CustomerDiscountDTO("0101010101", 2, 5000, 11)});
        assertEquals((1000000 * 149 * 1.25) * 0.89, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testSeventyninePercentOnNumberOfItemsBasisApplyDiscount() {
        sale.registerItem(1000000, new InventoryDTO(149.00, 25.0, "111", "Snow shovel"));
        item = sale.applyDiscount(new CustomerDiscountDTO[] {new CustomerDiscountDTO("0101010101", 3, 999999, 79)});
        assertEquals((1000000 * 149.0 * 1.25) * 0.21, item.getDiscountedTotal(), 0.01);
    }

    @Test
    public void testExtremeValueWithoutDiscountSubmitPayment() {
        sale.registerItem(1000000, new InventoryDTO(149.00, 25.0, "111", "Snow shovel"));
        item = sale.submitPayment(200000000);
        assertEquals(200000000 - (1000000 * 149 * 1.25), item.getChange(), 0.01);
    }

    @Test
    public void testExtremeValueWithFiftyFivePercentDiscountSubmitPayment() {
        sale.registerItem(1000000, new InventoryDTO(149.00, 25.0, "111", "Snow shovel"));
        sale.applyDiscount(new CustomerDiscountDTO[] {new CustomerDiscountDTO("0101010101", 3, 999999, 55)});
        item = sale.submitPayment(100000000);
        assertEquals(100000000 - ((1000000 * 149 * 1.25) * 0.45), item.getChange(), 0.01);
    }
}