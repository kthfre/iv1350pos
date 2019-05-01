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
     * Creates an instance of SaleDTO with change assigned.
     *
     * @param change The change to return to the customer.
     */

    public SaleDTO(double change) {
        this.change = change;
    }

    /**
     * Creates an instance of SaleDTO with description, price, and runningTotal assigned.
     *
     * @param description The current item description.
     * @param price The current item price.
     * @param runningTotal The current running total.
     */

    public SaleDTO(String description, double price, double runningTotal) {
        this.description = description;
        this.price = price;
        this.runningTotal = runningTotal;
    }

    /**
     * Creates an instance of SaleDTO with runningTotal, discountedTotal, and discountType assigned.
     *
     * @param runningTotal The current running total.
     * @param discountedTotal The current discounted total.
     * @param discountType The current discount type. 1 = per item, 2 = per number of total items, 3 = per total cost.
     */

    public SaleDTO(double runningTotal, double discountedTotal, int discountType) {
        this.runningTotal = runningTotal;
        this.discountedTotal = discountedTotal;
        this.discountType = discountType;
    }

    /**
     * Creates an instance of SaleDTO with dateTime, storeName, storeAddress, runningTotal, discountedTotal, vat, paid, change, and items assigned.
     *
     * @param receipt The receipt of the finalized sale.
     */

    public SaleDTO(Receipt receipt) {
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
