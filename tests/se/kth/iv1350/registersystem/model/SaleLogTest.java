package se.kth.iv1350.registersystem.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kth.iv1350.registersystem.dbhandler.InventoryDTO;

import static org.junit.Assert.*;

public class SaleLogTest {
    private Sale sale;
    private SaleLog saleLog;

    @Before
    public void setUp() {
        this.saleLog = new SaleLog();
    }

    @After
    public void tearDown() {
        this.sale = null;
        this.saleLog = null;
    }

    @Test
    public void testUninitiatedSaleLogSale() {
        saleLog.logSale(sale);
        assertEquals(0, saleLog.retrieveLogCount());
    }

    @Test
    public void testUnpaidSaleLogSale() {
        this.sale = new Sale();
        saleLog.logSale(sale);
        assertEquals(0, saleLog.retrieveLogCount());
    }

    @Test
    public void testWithItemsUnpaidSaleLogSale() {
        this.sale = new Sale();
        sale.registerItem(3, new InventoryDTO(21, 6.0, "123", "Great book"));
        sale.registerItem(2, new InventoryDTO(39.99, 12.0, "321", "Tomato soup"));
        saleLog.logSale(sale);
        assertEquals(0, saleLog.retrieveLogCount());
    }


    @Test
    public void testEmptyRetrieveLog() {
        assertNull("Unexpected return value (not null) as SaleLog should be empty.", saleLog.retrieveLog(0));
    }

    @Test
    public void testEmptyRetrieveLogCount() {
        assertEquals(0, saleLog.retrieveLogCount());
    }
}