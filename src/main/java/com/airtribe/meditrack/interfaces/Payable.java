package com.airtribe.meditrack.interfaces;

/**
 * Interface for entities that support payment/billing calculations.
 * Demonstrates interface with default and static methods.
 */
public interface Payable {

    /**
     * Calculate the total amount including taxes.
     *
     * @return total payable amount
     */
    double calculateTotal();

    /**
     * Apply a discount to the payable amount.
     *
     * @param discountPercent discount as a fraction (e.g., 0.10 for 10%)
     * @return discounted amount
     */
    double applyDiscount(double discountPercent);

    /**
     * Default method to generate a payment summary string.
     */
    default String getPaymentSummary() {
        return String.format("Total: $%.2f", calculateTotal());
    }

    /**
     * Static utility — calculate tax on a given amount.
     */
    static double calculateTax(double amount, double taxRate) {
        return amount * taxRate;
    }
}
