package se.kth.iv1350.registersystem.dbhandler;

/**
 * Exception that is thrown once a database query is made for an item that cannot be found.
 */

public class InventoryException extends Exception {
    private final String identifier;

    InventoryException(String identifier) {
        super("An attempt to search for a non-existing item with the current identifier was made " + identifier);
        this.identifier = identifier;
    }

    /**
     * Gets the item identifier of the iteam that was searched for once the exception was thrown.
     *
     * @return the item identifier.
     */

    public String getIdentifier() {
        return identifier;
    }
}
