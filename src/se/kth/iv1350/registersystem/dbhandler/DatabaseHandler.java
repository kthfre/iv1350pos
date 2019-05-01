package se.kth.iv1350.registersystem.dbhandler;

import se.kth.iv1350.registersystem.data.CustomerStorage;
import se.kth.iv1350.registersystem.data.InventoryStorage;

/**
 * Database handler, handling requests to and from storage solutions.
 */

public class DatabaseHandler {
    private final InventoryStorage inventoryStorageAPI;
    private final CustomerStorage customerStorageAPI;

    /**
     * Creates a new instance of DatabaseHandler combined a new instance of InventoryStorage and CustomerStorage.
     */

    public DatabaseHandler() {
        this.inventoryStorageAPI = new InventoryStorage();
        this.customerStorageAPI = new CustomerStorage();
    }

    /**
     * Gets inventory from inventory database.
     *
     * @param identifier The unique item identifier for which to query.
     * @return An object of the inventory with it's details, or if no match null.
     */

    public InventoryDTO selectFromInventory(String identifier) {
        return inventoryStorageAPI.getInventory(identifier);
    }

    /**
     * Gets customer from customer database joined with it's related discounts.
     *
     * @param personalNumber The unique personal number for which to query.
     * @return A collection of all matching discounts and their details, or if no matches an empty collection.
     */

    public CustomerDiscountDTO[] selectFromCustomer(String personalNumber) {
        return customerStorageAPI.getCustomerWithDiscounts(personalNumber);
    }
}
