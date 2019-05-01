package se.kth.iv1350.registersystem.dbhandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    public void testWithExistingSelectFromInventory() {
        inventory = db.selectFromInventory("321");
        assertEquals("Tomato soup", inventory.getDescription());
    }

    @Test
    public void testWithNonExistingSelectFromInventory() {
        inventory = db.selectFromInventory("987");
        assertNull("Unexpected return value (not null) when querying for a non-existing item.", inventory);
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
}