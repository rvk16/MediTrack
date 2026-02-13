package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.exception.InvalidDataException;

/**
 * Centralized validation utility for MediTrack.
 * Demonstrates: encapsulation of validation logic, static utility methods,
 * custom exception throwing with field context.
 */
public final class Validator {

    private Validator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidDataException(fieldName, fieldName + " cannot be null");
        }
    }

    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName, fieldName + " cannot be null or empty");
        }
    }

    public static void validateAge(int age) {
        if (age < Constants.MIN_AGE || age > Constants.MAX_AGE) {
            throw new InvalidDataException("age",
                    "Age must be between " + Constants.MIN_AGE + " and " + Constants.MAX_AGE);
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || !phone.matches("\\d{" + Constants.PHONE_LENGTH + "}")) {
            throw new InvalidDataException("phone",
                    "Phone must be exactly " + Constants.PHONE_LENGTH + " digits");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !email.matches(Constants.EMAIL_REGEX)) {
            throw new InvalidDataException("email", "Invalid email format");
        }
    }

    public static void validatePositive(double value, String fieldName) {
        if (value < 0) {
            throw new InvalidDataException(fieldName, fieldName + " must be non-negative");
        }
    }

    public static void validateId(String id, String entityName) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("id", entityName + " ID cannot be null or empty");
        }
    }
}
