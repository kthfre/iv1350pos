package se.kth.iv1350.registersystem.data;

class CustomerDiscountStorage {
    private Store[] store;
    private DiscountStorage discountStorage;

    CustomerDiscountStorage() {
        discountStorage = new DiscountStorage();
        this.store = simulateCustomerDiscount();
    }

    private class Store {
        private int customerId;
        private int discountTypeId;

        Store(int customerId, int discountTypeId) {
            this.customerId = customerId;
            this.discountTypeId = discountTypeId;
        }
    }

    private Store[] simulateCustomerDiscount() {
        return new Store[] {new Store(1, 1), new Store(1, 3),
                new Store(1, 6), new Store(2, 3), new Store(2, 4),
                new Store(3, 2), new Store(3, 5), new Store(3, 6),
                new Store(3, 7), new Store(4, 2), new Store(4, 7)};
    }

    int[][] joinOnId(int id) {
        int count = 0;

        for (int i = 0; i < store.length; i++) {
            if (store[i].customerId == id) {
                count++;
            }
        }

        int[] data = new int[count];
        count = 0;

        for (int i = 0; i < store.length; i++) {
            if (store[i].customerId == id) {
                data[count++] = store[i].discountTypeId;
            }
        }

        return discountStorage.getDiscount(data);
    }
}
