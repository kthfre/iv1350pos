package se.kth.iv1350.registersystem.dbhandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLTimeoutException;

import static org.junit.Assert.*;

public class DatabaseHandlerTest {
    private DatabaseHandler db;
    private InventoryDTO inventory;
    private CustomerDiscountDTO[] discounts;

    @Before
    public void setUp() {
        this.db = new DatabaseHandler();
    }

    @After
    public void tearDown() {
        this.db = null;
        this.inventory = null;
        this.discounts = null;
    }

    @Test
    public void testWithExistingSelectFromInventory() throws InventoryException {
        inventory = db.selectFromInventory("321");
        assertEquals("Tomato soup", inventory.getDescription());
    }

    @Test
    public void testWithNonExistingSelectFromInventory() throws InventoryException {
        try {
            inventory = db.selectFromInventory("987");
            fail("Doesn't throw appropriate exception when inventory doesn't exist.");
        } catch (InventoryException e) {
            assertEquals(e.getIdentifier(), "987");
        }
    }

    @Test
    public void testExistingSelectFromCustomer() {
        discounts = db.selectFromCustomer("0303030303");
        assertEquals(4, discounts.length);
    }

    @Test
    public void testTypesOfExistingSelectFromCustomer() {
        int[] typesCount = new int[] {0, 0, 0};
        int[] matchCount = new int[] {1, 1, 2};
        boolean match = true;
        discounts = db.selectFromCustomer("0303030303");

        for (int i = 0; i < discounts.length; i++) {
            typesCount[discounts[i].getDiscountType() - 1]++;
        }

        for (int i = 0; i < typesCount.length; i++) {
            if (typesCount[i] != matchCount[i]) {
                match = false;
            }
        }

        assertTrue("Discount count is not as expected according to predefined values.", match);
    }

    @Test
    public void testNonExistingSelectFromCustomer() {
        discounts = db.selectFromCustomer("0505050505");
        assertEquals(0, discounts.length);
    }

    @Test
    public void testSqlTimeoutDatabaseException() throws InventoryException {
        try {
            db.selectFromInventory("555");
            fail("Exception not thrown when database timed out.");
        } catch (DataRetrievalException e) {
            assertEquals(e.getIp(), "127.0.0.1");
        }
    }

    @Test
    public void testSqlNoTimeoutDatabaseException() throws InventoryException {
        try {
            inventory = db.selectFromInventory("123");
            assertEquals(inventory.getIdentifier(), "123");
        } catch (DataRetrievalException e) {
            fail("Exception thrown despite database not timing out.");
        }
    }

    @Test
    public void testNonExistingInventory() {
        try {
            db.selectFromInventory("333");
            fail("Exception not thrown when database timed out.");
        } catch (InventoryException e) {
            assertEquals(e.getIdentifier(), "333");
        }
    }

    @Test
    public void testExistingInventory() {
        try {
            inventory = db.selectFromInventory("321");
            assertEquals(inventory.getIdentifier(), "321");
        } catch (InventoryException e) {
            fail("Exception thrown despite inventory existing.");
        }
    }
}