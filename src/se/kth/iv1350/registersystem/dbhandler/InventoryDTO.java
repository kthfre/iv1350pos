package se.kth.iv1350.registersystem.dbhandler;

/**
 * DTO for transferring inventory in an OOP manner from storage to model given current storage solutions.
 */

public class InventoryDTO {
    private final double price;
    private final double vat;
    private final String identifier;
    private final String description;

    /**
     * Creates a new InventoryDTO instance with the arguments passed.
     *
     * @param price The price of the inventory.
     * @param vat The VAT of the inventory.
     * @param identifier The unique item identifier of the inventory.
     * @param description The description of the inventory.
     */

    public InventoryDTO(double price, double vat, String identifier, String description) {
        this.price = price;
        this.vat = vat;
        this.identifier = identifier;
        this.description = description;
    }

    /**
     * Gets the price of this inventory.
     *
     * @return The price of this InventoryDTO instance.
     */

    public double getPrice() {
        return price;
    }

    /**
     * Gets the VAT of this inventory.
     *
     * @return The VAT of this InventoryDTO instance.
     */

    public double getVat() {
        return vat;
    }

    /**
     * Gets the item identifier of this inventory.
     *
     * @return The unique item identifier of this InventoryDTO instance.
     */

    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the item description of this inventory.
     *
     * @return The description of this InventoryDTO instance.
     */

    public String getDescription() {
        return description;
    }
}
