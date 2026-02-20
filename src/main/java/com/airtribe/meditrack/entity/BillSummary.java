package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

/**
 * Immutable class representing a bill summary.
 * Demonstrates: immutability — final class, final fields, no setters, thread-safe by design.
 */
public final class BillSummary {

    private final String billId;
    private final String patientName;
    private final String doctorName;
    private final double consultationFee;
    private final double taxAmount;
    private final double discount;
    private final double totalAmount;
    private final LocalDateTime generatedAt;

    // Only constructor — all fields set here
    public BillSummary(String billId, String patientName, String doctorName,
                       double consultationFee, double taxAmount, double discount,
                       double totalAmount, LocalDateTime generatedAt) {
        this.billId = billId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.consultationFee = consultationFee;
        this.taxAmount = taxAmount;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.generatedAt = generatedAt;
    }

    // Only getters — no setters

    public String getBillId() {
        return billId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        return "BillSummary{billId='" + billId
                + "', patient='" + patientName
                + "', doctor='" + doctorName
                + "', total=$" + String.format("%.2f", totalAmount)
                + ", generatedAt=" + generatedAt + "}";
    }
}
