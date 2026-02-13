package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.interfaces.Payable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Represents a billing record for an appointment.
 * Demonstrates: interface implementation (Payable), polymorphism (generateBill overridable),
 * dynamic dispatch.
 * JPA: @Entity persisted to 'bills' table.
 */
@Entity
@Table(name = "bills")
public class Bill extends MedicalEntity implements Payable {

    @Column(name = "appointment_id", nullable = false)
    private String appointmentId;

    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "consultation_fee")
    private double consultationFee;

    @Column(name = "tax_amount")
    private double taxAmount;

    @Column
    private double discount;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "bill_type")
    private String billType; // STANDARD, INSURANCE, EMERGENCY

    @Column(name = "billed_at")
    private LocalDateTime billedAt;

    // No-arg constructor
    public Bill() {
        super();
        this.billedAt = LocalDateTime.now();
    }

    // Parameterized constructor
    public Bill(String id, String appointmentId, String patientId,
                String patientName, String doctorName,
                double consultationFee, String billType) {
        super(id);
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.consultationFee = consultationFee;
        this.billType = billType;
        this.billedAt = LocalDateTime.now();
        this.discount = 0.0;
        this.taxAmount = Payable.calculateTax(consultationFee, Constants.TAX_RATE);
        this.totalAmount = calculateTotal();
    }

    // --- Payable interface implementation ---

    @Override
    public double calculateTotal() {
        double afterDiscount = consultationFee - discount;
        this.taxAmount = Payable.calculateTax(afterDiscount, Constants.TAX_RATE);
        this.totalAmount = afterDiscount + taxAmount;
        return totalAmount;
    }

    @Override
    public double applyDiscount(double discountPercent) {
        this.discount = consultationFee * discountPercent;
        this.totalAmount = calculateTotal();
        return totalAmount;
    }

    // --- Polymorphism: generateBill can be overridden by subclasses ---
    public BillSummary generateBill() {
        calculateTotal();
        return new BillSummary(
                getId(), patientName, doctorName,
                consultationFee, taxAmount, discount, totalAmount, billedAt
        );
    }

    // --- MedicalEntity abstract methods ---

    @Override
    public String getEntityType() {
        return "Bill";
    }

    @Override
    public String getSummary() {
        return "Bill #" + getId() + " — " + patientName + " | Total: $"
                + String.format("%.2f", totalAmount);
    }

    // --- Getters and setters ---

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public LocalDateTime getBilledAt() {
        return billedAt;
    }

    public void setBilledAt(LocalDateTime billedAt) {
        this.billedAt = billedAt;
    }

    @Override
    public String toString() {
        return "Bill{id='" + getId() + "', patient='" + patientName
                + "', total=$" + String.format("%.2f", totalAmount) + "}";
    }
}
