package se.kth.iv1350.registersystem.model;

import se.kth.iv1350.registersystem.dbhandler.InventoryDTO;

/**
 * Entity describing item details and quantity of said item.
 */

public class Item {
    private int quantity;
    private final InventoryDTO itemDetails;

    /**
     * Creates a new Item instance with passed arguments;
     *
     * @param quantity The quantity of this item.
     * @param itemDetails The details of this item.
     */

    public Item(int quantity, InventoryDTO itemDetails) {
        this.quantity = quantity;
        this.itemDetails = itemDetails;
    }

    /**
     * Gets item quantity.
     *
     * @return Quantity of the referenced Item instance.
     */

    public int getQuantity() {
        return quantity;
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets item details.
     *
     * @return Item details of the referenced Item instance.
     */

    public InventoryDTO getItemDetails() {
        return itemDetails;
    }
}
