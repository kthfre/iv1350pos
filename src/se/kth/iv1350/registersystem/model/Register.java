package se.kth.iv1350.registersystem.model;

import se.kth.iv1350.registersystem.dbhandler.*;

/**
 * Register or cashiers system handling required sale related tasks.
 */

public class Register {
    private double balance = 0;
    private Sale sale;
    private final SaleLog saleLog;

    /**
     * Creates a new Register instance combined with a new instance of SaleLog.
     */

    public Register() {
        this.saleLog = new SaleLog();
    }

    /**
     * Creates a new Sale instance.
     */

    public void initiateSale() {
        this.sale = new Sale();
    }

    /**
     * Registers item the attached Sale instance.
     *
     * @param quantity The quantity of the item to be registered.
     * @param inventory The inventory details of the item to be registered.
     * @return An object containing the description and price of the item, as well as the running total of the sale. If quantity is invalid or inventory doesn't exist, returns an object which item description specifies it's invalid.
     */

    public SaleDTO registerItem(int quantity, InventoryDTO inventory) {
        return sale.registerItem(quantity, inventory);
    }

    /**
     * Applies discount, if available, to current sale.
     *
     * @param discounts The discounts to be applied.
     * @return An object containing running total, discounted total and discount type.
     */

    public SaleDTO applyDiscount(CustomerDiscountDTO[] discounts) {
        return sale.applyDiscount(discounts);
    }

    /**
     * Submits payment, and if sufficient, finalizes and logs current sale.
     *
     * @param amount The amount to be paid.
     * @return An object containing the sale details of the receipt. If the sale was not finalized, the amount by which the payment was short.
     */

    public SaleDTO submitPayment(double amount) {
        SaleDTO saleDetails = sale.submitPayment(amount);
        boolean salePaid = saleDetails.getChange() >= 0;

        if (salePaid) {
            this.balance += amount - saleDetails.getChange();
            logSale();
        }

        return saleDetails;
    }

    /**
     * Logs the sale.
     */

    void logSale() {
        saleLog.logSale(sale);
    }

    /**
     * Retrieves receipt for a specified sale that has been logged.
     *
     * @param index The index of the sale to be retrieved. Logs are stored in chronological order where higher index means older sale.
     * @return The sale details of the receipt. If sale doesn't exist, returns null.
     */

    public SaleDTO retrieveLog(int index) {
        return saleLog.retrieveLog(index);
    }

    /**
     * Retrieves number of logged sales.
     *
     * @return The number of sales that have been logged in the SaleLog.
     */

    public int retrieveLogCount() {
        return saleLog.retrieveLogCount();
    }

    /**
     * Gets the balance of this register.
     *
     * @return The balance of this Register instance.
     */

    public double getBalance() {
        return balance;
    }

    /**
     * Interprets if the input data matches the given pattern.
     *
     * @param input The input to be tested.
     * @param pattern The string or pattern to test the input against.
     * @return A boolean <code>true</code> if matches, <code>false</code> otherwise.
     */

    public boolean interpretInput(String input, String pattern) {
        return input.matches(pattern);
    }
}
