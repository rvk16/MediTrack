package com.airtribe.meditrack.factory;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.util.IdGenerator;

/**
 * Factory for creating Bill objects of different types.
 * Demonstrates: Factory design pattern — centralizes Bill creation logic.
 */
public class BillFactory {

    private BillFactory() {
        throw new UnsupportedOperationException("Use static factory methods");
    }

    /**
     * Create a standard consultation bill.
     */
    public static Bill createStandardBill(Appointment appointment, Doctor doctor, Patient patient) {
        return new Bill(
                IdGenerator.getInstance().nextBillId(),
                appointment.getId(),
                patient.getId(),
                patient.getName(),
                doctor.getName(),
                doctor.getConsultationFee(),
                "STANDARD"
        );
    }

    /**
     * Create an insurance-covered bill.
     */
    public static Bill createInsuranceBill(Appointment appointment, Doctor doctor, Patient patient) {
        return new Bill(
                IdGenerator.getInstance().nextBillId(),
                appointment.getId(),
                patient.getId(),
                patient.getName(),
                doctor.getName(),
                doctor.getConsultationFee(),
                "INSURANCE"
        );
    }

    /**
     * Create an emergency bill with surcharge.
     */
    public static Bill createEmergencyBill(Appointment appointment, Doctor doctor, Patient patient) {
        double emergencyFee = doctor.getConsultationFee() * 1.5; // 50% surcharge
        return new Bill(
                IdGenerator.getInstance().nextBillId(),
                appointment.getId(),
                patient.getId(),
                patient.getName(),
                doctor.getName(),
                emergencyFee,
                "EMERGENCY"
        );
    }

    /**
     * Generic factory method — creates bill based on type string.
     * Demonstrates: dynamic dispatch via factory.
     */
    public static Bill createBill(String billType, Appointment appointment, Doctor doctor, Patient patient) {
        return switch (billType.toUpperCase()) {
            case "INSURANCE" -> createInsuranceBill(appointment, doctor, patient);
            case "EMERGENCY" -> createEmergencyBill(appointment, doctor, patient);
            default -> createStandardBill(appointment, doctor, patient);
        };
    }
}
