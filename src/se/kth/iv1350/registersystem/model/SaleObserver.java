package se.kth.iv1350.registersystem.model;

/**
 * Contract for sale observation.
 */

public interface SaleObserver {

    /**
     * Update total amount of sale.
     *
     * @param amount the amount.
     */

public void update(double amount);
}
