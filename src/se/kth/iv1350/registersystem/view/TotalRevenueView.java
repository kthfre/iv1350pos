package se.kth.iv1350.registersystem.view;

import se.kth.iv1350.registersystem.model.SaleObserver;

/**
 * Display and keeps track of total amount of current sales since system start.
 */

public class TotalRevenueView implements SaleObserver {
    private double totalPaidSinceSystemStartup = 0;

    TotalRevenueView() {

    }

    /**
     * Updates the current total amount and calls the print method.
     *
     * @param amount the amount added to the total.
     */

    public void update(double amount) {
        this.totalPaidSinceSystemStartup += amount;
        printCurrentTotal();
    }

    private void printCurrentTotal() {
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        System.out.println("For cashiers eyes only, total amount sold for since log on: " + View.toTwoDec(totalPaidSinceSystemStartup));
        System.out.println("------------------------------------------------------------------");
        System.out.println();
    }
}
