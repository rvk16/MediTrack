package com.airtribe.meditrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Global exception handler for REST API.
 * Demonstrates: Spring @RestControllerAdvice, centralized error handling.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidData(InvalidDataException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getFieldName());
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAppointmentNotFound(AppointmentNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "appointmentId");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: " + ex.getMessage(), null);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, String field) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (field != null) {
            body.put("field", field);
        }
        return new ResponseEntity<>(body, status);
    }
}
