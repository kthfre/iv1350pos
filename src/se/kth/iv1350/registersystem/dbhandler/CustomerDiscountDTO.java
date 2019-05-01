package se.kth.iv1350.registersystem.dbhandler;

/**
 * DTO for transferring customer discounts in an OOP manner from storage to model given current storage solutions.
 */

public class CustomerDiscountDTO {
    private final String personalNumber;
    private final int discountType;
    private final int threshold;
    private final int rate;

    /**
     * Creates a new instance of CustomerDiscountDTO with passed arguments.
     *
     * @param personalNumber The personal number that identifies the customer.
     * @param discountType The discount type.
     * @param threshold The requirement for the discount.
     * @param rate The discount rate. Must be in interval 0-100.
     */

    public CustomerDiscountDTO(String personalNumber, int discountType, int threshold, int rate) {
        this.personalNumber = personalNumber;
        this.discountType = discountType;
        this.threshold = threshold;
        this.rate = rate < 0 ? 0 : rate > 100 ? 100 : rate;
    }

    /**
     * Gets the discount type for this discount.
     *
     * @return The numerical code for the discount type. 1 = per item, 2 = per total number of items, 3 = per total cost.
     */

    public int getDiscountType() {
        return discountType;
    }

    /**
     * Gets the requirements for this discount.
     *
     * @return The requirement. If getDiscountType() == 1 it's the item identifier, if getDiscountType() == 2 it's the required number of items, if getDiscountType() == 3 it's the required total cost.
     */

    public int getThreshold() {
        return threshold;
    }

    /**
     * Gets the rate for this discount.
     *
     * @return The discount rate as an integer between 0-100.
     */

    public int getRate() {
        return rate;
    }
}
