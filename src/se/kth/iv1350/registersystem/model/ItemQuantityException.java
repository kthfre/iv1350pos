package se.kth.iv1350.registersystem.model;

/**
 * Exception to be thrown if an invalid quantity is given during the registration process.
 */

public class ItemQuantityException extends Exception {
    private int quantity;

    ItemQuantityException(int quantity) {
        super("Attempt to register with quantity " + quantity + " which is invalid.");
        this.quantity = quantity;
    }

    /**
     * Returns the invalid quantity of the thrown exception.
     *
     * @return the invalid quantity.
     */

    public int getQuantity() {
        return quantity;
    }
}
