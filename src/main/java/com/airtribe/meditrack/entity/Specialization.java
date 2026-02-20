package com.airtribe.meditrack.entity;

/**
 * Enum representing medical specializations for doctors.
 * Demonstrates enum with fields, constructor, and methods.
 */
public enum Specialization {

    CARDIOLOGY("Cardiology", "Heart and cardiovascular system"),
    DERMATOLOGY("Dermatology", "Skin, hair, and nails"),
    NEUROLOGY("Neurology", "Brain and nervous system"),
    ORTHOPEDICS("Orthopedics", "Bones and joints"),
    PEDIATRICS("Pediatrics", "Children's health"),
    GENERAL("General Medicine", "General health and wellness"),
    ENT("ENT", "Ear, Nose, and Throat"),
    OPHTHALMOLOGY("Ophthalmology", "Eye care"),
    PSYCHIATRY("Psychiatry", "Mental health"),
    GYNECOLOGY("Gynecology", "Women's health");

    private final String displayName;
    private final String description;

    Specialization(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
