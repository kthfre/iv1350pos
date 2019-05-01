package se.kth.iv1350.registersystem.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Receipt that holds sale details and proofs purchase.
 */

public class Receipt {
    private final DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.UK).withZone(ZoneId.systemDefault());
    private final Instant dateTime;
    private final String storeName;
    private final Address storeAddress;
    private final List<Item> items;
    private final double total;
    private final double discountedTotal;
    private final double vat;
    private final double paid;
    private final double change;

    Receipt(String storeName, Address storeAddress, List<Item> items, double total, double discountedTotal, double vat, double paid, double change) {
        this.dateTime = Instant.now();
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.items = new ArrayList<>(items);
        this.total = total;
        this.discountedTotal = discountedTotal;
        this.vat = vat;
        this.paid = paid;
        this.change = change;
    }

    /**
     * Gets creation time and date of receipt.
     *
     * @return Date and time of referenced Receipt instance.
     */

    public Instant getDateTime() {
        return dateTime;
    }

    /**
     * Gets store name of receipt.
     *
     * @return Store name of referenced Receipt instance.
     */

    public String getStoreName() {
        return storeName;
    }

    /**
     * Gets store address of this receipt.
     *
     * @return Store address of referenced Receipt instance.
     */

    public Address getStoreAddress() {
        return storeAddress;
    }

    /**
     * Gets list of items from the sale that produced this receipt.
     *
     * @return List of items of referenced Receipt instance.
     */

    public List<Item> getItems() {
        return items;
    }

    /**
     * Gets total cost of sale that produced this receipt.
     *
     * @return The total cost of the Sale instance that this Receipt instance is attached to.
     */

    public double getTotal() {
        return total;
    }

    /**
     * Gets the discounted total cost of sale that produced this receipt.
     *
     * @return The discounted total cost of the Sale instance that this Receipt instance is attached to. Equal to getTotal() if no discount has been applied.
     */

    public double getDiscountedTotal() {
        return discountedTotal;
    }

    /**
     * Gets the VAT of the sale that produced this receipt.
     *
     * @return The VAT of the Sale instance that this Receipt instance is attached to.
     */

    public double getVat() {
        return vat;
    }

    /**
     * Gets the amount the customer paid to finalize the sale.
     *
     * @return The amount paid for the Sale instance that this Receipt instance is attached to. getPaid() larger or equal than getDiscountedTotal or this Receipt would not be instantiated.
     */

    public double getPaid() {
        return paid;
    }

    /**
     * Gets the change returned to the customer after finalizing the sale.
     *
     * @return The change to be returned to the customer for what was paid for the Sale instance that this Receipt instance is attached to. That is, the difference between getPaid() - getDiscountedTotal().
     */

    public double getChange() {
        return change;
    }

    /**
     * Gets a date/time formatter.
     *
     * @return An object containing functionality to format the date and time into human readable format.
     */

    public DateTimeFormatter getFormat() {
        return format;
    }
}
