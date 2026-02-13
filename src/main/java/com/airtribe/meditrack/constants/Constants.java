package com.airtribe.meditrack.constants;

/**
 * Application-wide constants for MediTrack.
 * Demonstrates use of static final fields and static initialization blocks.
 */
public final class Constants {

    // Private constructor prevents instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    // Tax & billing
    public static final double TAX_RATE = 0.18;
    public static final double INSURANCE_DISCOUNT = 0.15;

    // File paths for CSV persistence
    public static final String DATA_DIRECTORY = "data/";
    public static final String PATIENTS_CSV = DATA_DIRECTORY + "patients.csv";
    public static final String DOCTORS_CSV = DATA_DIRECTORY + "doctors.csv";
    public static final String APPOINTMENTS_CSV = DATA_DIRECTORY + "appointments.csv";
    public static final String BILLS_CSV = DATA_DIRECTORY + "bills.csv";

    // Validation
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 150;
    public static final int PHONE_LENGTH = 10;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    // Application info — initialized via static block
    public static final String APP_NAME;
    public static final String APP_VERSION;

    static {
        APP_NAME = "MediTrack";
        APP_VERSION = "1.0.0";
        System.out.println("[Static Block] Constants class loaded — " + APP_NAME + " v" + APP_VERSION);
    }
}
