package se.kth.iv1350.registersystem.data;

import se.kth.iv1350.registersystem.dbhandler.InventoryDTO;

/**
 * Inventory storage. Attempts to simulate database table.
 */

public class InventoryStorage {
    private static final InventoryStorage INVENTORY_STORAGE = new InventoryStorage();
    private Store[] store;

    /**
     * Creates a new InventoryStorage instance and populates storage with sample data.
     */

 /*   public InventoryStorage() {
        this.store = simulateInventory();
    } */

    private InventoryStorage() {
        this.store = simulateInventory();
    }

    /**
     * Gets the current InventoryStorage instance.
     *
     * @return the current InventoryStorage instance.
     */

    public static InventoryStorage getInventoryStorage() {
        return INVENTORY_STORAGE;
    }

    private class Store {
        private int id;
        private double price;
        private double vat;
        private String identifier;
        private String description;

        Store(int id, double price, double vat, String identifier, String description) {
            this.id = id;
            this.price = price;
            this.vat = vat;
            this.identifier = identifier;
            this.description = description;
        }
    }

    private Store[] simulateInventory() {
        return new Store[] {new Store(1, 21.00, 6.00, "123", "Great book!"),
                new Store(2, 39.99, 12.00, "321", "Tomato soup"), new Store(3, 149.00, 25.00, "111", "Snow shovel"),
                new Store(4, 4.99, 25.00, "222", "Premium pen")};
    }

    /**
     * Gets inventory from storage.
     *
     * @param identifier The unique item identifier to be searched for.
     * @return The item details of the item matching the identifier, or null if no match is found.
     */

    public InventoryDTO getInventory(String identifier) {
        for (int i = 0; i < store.length; i++) {
            if (store[i].identifier.equals(identifier)) {
                return new InventoryDTO(store[i].price, store[i].vat, store[i].identifier, store[i].description);
            }
        }

        return null;
    }
}
