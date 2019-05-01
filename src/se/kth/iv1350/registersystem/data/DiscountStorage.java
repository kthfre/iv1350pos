package se.kth.iv1350.registersystem.data;

class DiscountStorage {
    private Store[] store;

    DiscountStorage() {
        this.store = simulateDiscount();
    }

    private class Store {
        private int id;
        private int type;
        private int threshold;
        private int rate;

        Store(int id, int type, int threshold, int rate) {
            this.id = id;
            this.type = type;
            this.threshold = threshold;
            this.rate = rate;
        }
    }

    private Store[] simulateDiscount() {
        return new Store[] {new Store(1, 1, 321, 20), new Store(2, 1, 321, 10),
            new Store(3, 1, 123, 10), new Store(4, 2, 10, 10), new Store(5, 2, 10, 15),
            new Store(6, 3, 150, 20), new Store(7, 3, 250, 30)};
    }

    int[][] getDiscount(int[] discounts) {
        int count = 0;

        for (int i = 0; i < store.length; i++) {
            for (int j = 0; j < discounts.length; j++) {
                if (store[i].id == discounts[j]) {
                    count++;
                }
            }
        }

        int[][] data = new int[count][3];
        count = 0;

        for (int i = 0; i < store.length; i++) {
            for (int j = 0; j < discounts.length; j++) {
                if (store[i].id == discounts[j]) {
                    data[count][0] = store[i].type;
                    data[count][1] = store[i].threshold;
                    data[count++][2] = store[i].rate;
                }
            }
        }

        return data;
    }
}
