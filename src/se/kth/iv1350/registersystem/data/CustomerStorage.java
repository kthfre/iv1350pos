package se.kth.iv1350.registersystem.data;

import se.kth.iv1350.registersystem.dbhandler.CustomerDiscountDTO;

/**
 * Customer storage. Attempts to simulate database table.
 */

public class CustomerStorage {
    private Store[] store;
    private final CustomerDiscountStorage customerDiscountStorage;

    /**
     * Creates a new instance of CustomerStorage combined with a new instance of CustomerDiscountStorage. Populates storage with sample data.
     */

    public CustomerStorage() {
        customerDiscountStorage = new CustomerDiscountStorage();
        this.store = simulateCustomer();
    }

    private class Store {
        private int id;
        private String personalNumber;
        private String firstName;
        private String lastName;

        Store(int id, String personalNumber, String firstName, String lastName) {
            this.id = id;
            this.personalNumber = personalNumber;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    private Store[] simulateCustomer() {
        return new Store[] {new Store(1, "0101010101", "Johan", "Johansson"),
                new Store(2, "0202020202", "Karl", "Karlsson"), new Store(3, "0303030303", "Kristina", "Kristersson"),
                new Store(4, "0404040404", "Johanna", "Johansson")};
    }

    /**
     * Gets customer with it's related discounts.
     *
     * @param personalNumber The specified personal number which identifies each customer.
     * @return The collection of discounts associated with the customer, or an empty such collection if no discounts are found.
     */

    public CustomerDiscountDTO[] getCustomerWithDiscounts(String personalNumber) {
        Store customer = selectCustomer(personalNumber);
        CustomerDiscountDTO[] customerDiscounts;
        boolean customerExists = customer.id > 0;

        if (!customerExists) {
            return new CustomerDiscountDTO[0];
        }

        int[][] discounts = customerDiscountStorage.joinOnId(customer.id);
        customerDiscounts = new CustomerDiscountDTO[discounts.length];

        for (int i = 0; i < discounts.length; i++) {
            customerDiscounts[i] = new CustomerDiscountDTO(customer.personalNumber, discounts[i][0], discounts[i][1], discounts[i][2]);
        }

        return customerDiscounts;
    }

    private Store selectCustomer(String personalNumber) {
        for (int i = 0; i < store.length; i++) {
            if (store[i].personalNumber.equals(personalNumber)) {
                return store[i];
            }
        }

        return new Store(0, "", "N/A", "N/A");
    }
}
