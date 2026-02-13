package com.airtribe.meditrack.exception;

/**
 * Custom exception for invalid input data.
 * Demonstrates custom exceptions with exception chaining.
 */
public class InvalidDataException extends RuntimeException {

    private final String fieldName;

    public InvalidDataException(String message) {
        super(message);
        this.fieldName = null;
    }

    public InvalidDataException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    // Exception chaining — wrapping a root cause
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
        this.fieldName = null;
    }

    public InvalidDataException(String fieldName, String message, Throwable cause) {
        super(message, cause);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
