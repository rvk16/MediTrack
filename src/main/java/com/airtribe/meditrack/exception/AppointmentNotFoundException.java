package com.airtribe.meditrack.exception;

/**
 * Custom exception thrown when an appointment cannot be found.
 * Demonstrates custom checked-style exception (extends RuntimeException for REST convenience).
 */
public class AppointmentNotFoundException extends RuntimeException {

    private final String appointmentId;

    public AppointmentNotFoundException(String appointmentId) {
        super("Appointment not found with ID: " + appointmentId);
        this.appointmentId = appointmentId;
    }

    // Exception chaining
    public AppointmentNotFoundException(String appointmentId, Throwable cause) {
        super("Appointment not found with ID: " + appointmentId, cause);
        this.appointmentId = appointmentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }
}
