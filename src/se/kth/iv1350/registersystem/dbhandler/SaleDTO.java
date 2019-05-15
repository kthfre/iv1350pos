package se.kth.iv1350.registersystem.dbhandler;

import se.kth.iv1350.registersystem.model.Item;
import se.kth.iv1350.registersystem.model.Receipt;

/**
 * DTO for transferring sale details in an OOP manner from model to view given current implementation of command line interface.
 */

public class SaleDTO {
    private String description;
    private double price;
    private double runningTotal;
    private double discountedTotal;
    private double vat;
    private int discountType;
    private double paid;
    private double change;
    private String dateTime;
    private String storeName;
    private String storeAddress;
    private Item[] items;

    /**
     * Builder class for SaleDTO instance creation.
     */

    public static class Builder {
        private String description;
        private double price;
        private double runningTotal;
        private double discountedTotal;
        private double vat;
        private int discountType;
        private double paid;
        private double change;
        private String dateTime;
        private String storeName;
        private String storeAddress;
        private Item[] items;

        /**
         * Add description.
         *
         * @param description the description to be added.
         * @return the current object.
         */

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Add price.
         *
         * @param price the price to be added.
         * @return the current object.
         */

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        /**
         * Add running total..
         *
         * @param runningTotal the total to be added.
         * @return the current object.
         */

        public Builder runningTotal(double runningTotal) {
            this.runningTotal = runningTotal;
            return this;
        }

        /**
         * Add discounted total.
         *
         * @param discountedTotal the total to be added.
         * @return the current object.
         */

        public Builder discountedTotal(double discountedTotal) {
            this.discountedTotal = discountedTotal;
            return this;
        }

        /**
         * Add VAT.
         *
         * @param vat the vat to be added.
         * @return the current object.
         */

        public Builder vat(double vat) {
            this.vat = vat;
            return this;
        }

        /**
         * Add discount type.
         *
         * @param discountType the discount type to be added.
         * @return the current object.
         */

        public Builder discountType(int discountType) {
            this.discountType = discountType;
            return this;
        }

        /**
         * Add paid amount.
         *
         * @param paid the paid amount to be added.
         * @return the current object.
         */

        public Builder paid(double paid) {
            this.paid = paid;
            return this;
        }

        /**
         * Add the change.
         *
         * @param change the change to be added.
         * @return the current object.
         */

        public Builder change(double change) {
            this.change = change;
            return this;
        }

        /**
         * Add the date and time.
         *
         * @param dateTime the date and time to be added.
         * @return the current object.
         */

        public Builder dateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        /**
         * Add store name.
         *
         * @param storeName the store name to be added.
         * @return the current object.
         */

        public Builder storeName(String storeName) {
            this.storeName = storeName;
            return this;
        }

        /**
         * Add store address.
         *
         * @param storeAddress the store address to be added.
         * @return the current object.
         */

        public Builder storeAddress(String storeAddress) {
            this.storeAddress = storeAddress;
            return this;
        }

        /**
         * Add items.
         *
         * @param items the items to be added.
         * @return the current object.
         */

        public Builder items(Item[] items) {
            this.items = items;
            return this;
        }

        /**
         * Add required details from a receipt.
         *
         * @param receipt the receipt that holds the details to be added.
         * @return the current object.
         */

        public Builder fromReceipt(Receipt receipt) {
            this.dateTime = receipt.getFormat().format(receipt.getDateTime());
            this.storeName = receipt.getStoreName();
            this.storeAddress = receipt.getStoreAddress().getStreetName() + " " + receipt.getStoreAddress().getStreetNumber() + " " + receipt.getStoreAddress().getCity();
            this.runningTotal = receipt.getTotal();
            this.discountedTotal = receipt.getDiscountedTotal();
            this.vat = receipt.getVat();
            this.paid = receipt.getPaid();
            this.change = receipt.getChange();
            this.items = new Item[receipt.getItems().size()];

            for (int i = 0; i < receipt.getItems().size(); i++) {
                items[i] = receipt.getItems().get(i);
            }

            return this;
        }

        /**
         * Builds the SaleDTO instance.
         *
         * @return the SaleDTO instance.
         */

        public SaleDTO build() {
            return new SaleDTO(this);
        }
    }

    private SaleDTO(Builder builder) {
        this.description = builder.description;
        this.price = builder.price;
        this.runningTotal = builder.runningTotal;
        this.discountedTotal = builder.discountedTotal;
        this.vat = builder.vat;
        this.discountType = builder.discountType;
        this.paid = builder.paid;
        this.change = builder.change;
        this.dateTime = builder.dateTime;
        this.storeName = builder.storeName;
        this.storeAddress = builder.storeAddress;
        this.items = builder.items;
    }

    /**
     * Gets description.
     *
     * @return The current item description.
     */

    public String getDescription() {
        return description;
    }

    /**
     * Gets price.
     *
     * @return The current item price.
     */

    public double getPrice() {
        return price;
    }

    /**
     * Gets running total.
     *
     * @return The current running total.
     */

    public double getRunningTotal() {
        return runningTotal;
    }

    /**
     * Gets discounted total.
     *
     * @return The current discounted total.
     */

    public double getDiscountedTotal() {
        return discountedTotal;
    }

    /**
     * Gets discount type.
     *
     * @return The discount type of the just applied discount, numerical codes: -1 = not eligible for discounts, 0 = invalid customer, 1 = per item discount applied, 2 = per nuumber of total items discount applied, 3 = per total cost discount applied..
     */

    public int getDiscountType() {
        return discountType;
    }

    /**
     * Gets change.
     *
     * @return The change the customer shall receive after payment of the current sale, if negative payment was insufficient.
     */

    public double getChange() {
        return change;
    }

    /**
     * Gets VAT.
     *
     * @return The VAT of the finalized sale.
     */

    public double getVat() {
        return vat;
    }

    /**
     * Gets paid amount.
     *
     * @return The paid amount of the finalized sale.
     */

    public double getPaid() {
        return paid;
    }

    /**
     * Gets date and time of the registration of the purchase.
     *
     * @return The date and time registered on the receipt in a human readable format.
     */

    public String getDateTime() {
        return dateTime;
    }

    /**
     * Gets the store name.
     *
     * @return The store name.
     */

    public String getStoreName() {
        return storeName;
    }

    /**
     * Gets the store address.
     *
     * @return The store address.
     */

    public String getStoreAddress() {
        return storeAddress;
    }

    /**
     * Gets the items of the sale.
     *
     * @return The items, with their quantity and their details, of the sale.
     */

    public Item[] getItems() {
        return items;
    }
}
