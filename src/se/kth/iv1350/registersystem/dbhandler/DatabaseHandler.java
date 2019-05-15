package se.kth.iv1350.registersystem.dbhandler;

import se.kth.iv1350.registersystem.data.CustomerStorage;
import se.kth.iv1350.registersystem.data.InventoryStorage;

import java.sql.SQLTimeoutException;

/**
 * Database handler, handling requests to and from storage solutions.
 */

public class DatabaseHandler {
    private final InventoryStorage inventoryStorageAPI;
    private final CustomerStorage customerStorageAPI;
    private static final String SERVER_IP_ADDRESS = "127.0.0.1";
    private static final String SERVER_PORT = "1433";

    /**
     * Creates a new instance of DatabaseHandler combined a new instance of InventoryStorage and CustomerStorage.
     */

    public DatabaseHandler() {
        this.inventoryStorageAPI = InventoryStorage.getInventoryStorage(); // new InventoryStorage();
        this.customerStorageAPI = new CustomerStorage();
    }

    /**
     * Gets inventory from inventory database.
     *
     * @param identifier The unique item identifier for which to query.
     * @throws InventoryException if an item inventory which can't be found in database is passed.
     * @throws DataRetrievalException if there is an issue with the data retrieval process along with details about said issue. There is currently only support database time out.
     * @return An object of the inventory with it's details, or if no match null.
     */

    public InventoryDTO selectFromInventory(String identifier) throws InventoryException {
        InventoryDTO inventory = inventoryStorageAPI.getInventory(identifier);

        if (identifier == null || identifier.equals("555")) {
            try {
                throw new SQLTimeoutException("Connection timed out.");
            } catch (SQLTimeoutException e) {
                throw new DataRetrievalException(SERVER_IP_ADDRESS, SERVER_PORT, "connection timed out", "12:04", identifier, e);
            }
        }

        if (inventory == null) {
            throw new InventoryException(identifier);
        }

        return inventory;
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
