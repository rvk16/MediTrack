package com.airtribe.meditrack.entity;

/**
 * Enum representing the status of an appointment.
 * Replaces raw strings with type-safe constants.
 */
public enum AppointmentStatus {

    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    NO_SHOW("No Show");

    private final String displayName;

    AppointmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
