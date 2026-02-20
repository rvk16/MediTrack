package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.Bill;
import org.springframework.stereotype.Component;

/**
 * Insurance billing strategy — applies insurance discount before tax.
 * Demonstrates: Strategy pattern concrete implementation.
 */
@Component
public class InsuranceBillingStrategy implements BillingStrategy {

    @Override
    public double calculate(Bill bill) {
        double fee = bill.getConsultationFee();
        double discount = fee * Constants.INSURANCE_DISCOUNT;
        double afterDiscount = fee - discount;
        double tax = afterDiscount * Constants.TAX_RATE;
        double total = afterDiscount + tax;

        bill.setDiscount(discount);
        bill.setTaxAmount(tax);
        bill.setTotalAmount(total);
        return total;
    }

    @Override
    public String getStrategyName() {
        return "INSURANCE";
    }
}
