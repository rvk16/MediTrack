package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Bill;

/**
 * Strategy pattern interface for different billing strategies.
 * Demonstrates: Strategy design pattern, interface-based polymorphism.
 */
public interface BillingStrategy {

    /**
     * Calculate the bill using this strategy.
     *
     * @param bill the bill to calculate
     * @return calculated total amount
     */
    double calculate(Bill bill);

    /**
     * Get the name of this billing strategy.
     */
    String getStrategyName();
}
