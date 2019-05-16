package se.kth.iv1350.registersystem.controller;

import se.kth.iv1350.registersystem.model.*;
import se.kth.iv1350.registersystem.dbhandler.*;
import se.kth.iv1350.registersystem.utils.LogHandler;
import se.kth.iv1350.registersystem.view.TotalRevenueView;

/**
 * The Controller - per default an intermediate layer.
 */

public class Controller {
    private final Register register;
    private final DatabaseHandler db;
    private final LogHandler log;

    /**
     * Creates a new Controller instance combined with a new Register and DatabaseHandler instance. Each Controller object is immutable.
     */

    public Controller() {
        this.register = new Register();
        this.db = new DatabaseHandler();
        this.log = new LogHandler();
    }

    /**
     * Initiates a new sale.
     */

    public void initiateSale() {
        register.initiateSale();
    }

    /**
     * Retrieves inventory from database and attempts to register specified quantity of retrieved inventory in the sale.
     *
     * @param quantity The quantity of the inventory to be registered.
     * @param identifier The inventory to be registered. If inventory doesn't exist, null is passed.
     * @throws FatalException if there is a database time out failure to allow for a clean exit from the unrecoverable exception.
     * @throws GeneralFailureException if an item identifier for a non-existing item is passed, or an invalid quantity is passed, along with a message.
     * @return An object containing the description and price of the item, as well as the running total of the sale. If quantity is invalid or inventory doesn't exist, returns an object which item description specifies it's invalid.
     */

    public SaleDTO scanItem(int quantity, String identifier) throws GeneralFailureException {
        try {
            return register.registerItem(quantity, db.selectFromInventory(identifier));
        } catch (InventoryException e) {
            log.consoleLog(e.getMessage(), e);
            throw new GeneralFailureException("item not found", e);
        } catch (ItemQuantityException e) {
            log.consoleLog(e.getMessage(), e);
            throw new GeneralFailureException("faulty quantity", e);
        } catch (DataRetrievalException e) {
            log.consoleLog("Data retrieval failed, given reason: " + e.getReason() + ".\n" +
                    "SQL Server " + e.getIp() + ":" + e.getPort() + "; SELECT * FROM Inventory i WHERE i.id = \"" + e.getIdentifier() + "\";", e);
            throw new FatalException();
        }
    }

    /**
     * Retrieves customer from database and attempts to apply discount to current sale.
     *
     * @param id The personal number identifying the customer.
     * @return An object containing running total, discounted total and discount type.
     */

    public SaleDTO applyDiscount(String id) {
        return register.applyDiscount(db.selectFromCustomer(id));
    }

    /**
     * Pays the sale.
     *
     * @param amount The amount being paid.
     * @return An object containing the sale details of the receipt. If the sale was not finalized, the amount by which the payment was short.
     */

    public SaleDTO pay(double amount) {
        return register.submitPayment(amount);
    }

    /**
     * Retrieves receipt of specified sale from SaleLog.
     *
     * @param index The index of the sale to be retrieved. Logs are stored in chronological order where higher index means older sale.
     * @return The sale details of the receipt. If sale doesn't exist, returns null.
     */

    public SaleDTO retrieveLog(int index) {
        return register.retrieveLog(index);
    }

    /**
     * Retrieves number of logged sales.
     *
     * @return The number of sales that have been logged in the SaleLog.
     */

    public int retrieveLogCount() {
        return register.retrieveLogCount();
    }

    /**
     * Interprets if the input data matches the given pattern.
     *
     * @param input The input to be tested.
     * @param pattern The string or pattern to test the input against.
     * @return A boolean <code>true</code> if matches, <code>false</code> otherwise.
     */

    public boolean interpretInput(String input, String pattern) {
        return register.interpretInput(input, pattern);
    }

    /**
     * Adds observer to list in current Register instance.
     *
     * @param totalRevenueView the observer to be added.
     */

    public void addRegisterObserver(SaleObserver observer) {
        register.attachObserver(observer);
    }
}
