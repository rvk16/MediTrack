package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.Bill;
import org.springframework.stereotype.Component;

/**
 * Standard billing strategy — applies full tax, no discount.
 * Demonstrates: Strategy pattern concrete implementation.
 */
@Component
public class StandardBillingStrategy implements BillingStrategy {

    @Override
    public double calculate(Bill bill) {
        double fee = bill.getConsultationFee();
        double tax = fee * Constants.TAX_RATE;
        bill.setTaxAmount(tax);
        bill.setDiscount(0);
        double total = fee + tax;
        bill.setTotalAmount(total);
        return total;
    }

    @Override
    public String getStrategyName() {
        return "STANDARD";
    }
}
