package com.airtribe.meditrack.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Date utility class for MediTrack.
 * Demonstrates: static utility methods, exception handling, Java date/time API.
 */
public final class DateUtil {

    public static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static final DateTimeFormatter ISO_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private DateUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String formatForDisplay(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(DISPLAY_FORMAT);
    }

    public static String formatForStorage(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(ISO_FORMAT);
    }

    public static LocalDateTime parseFromStorage(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) return null;
        try {
            return LocalDateTime.parse(dateString.trim(), ISO_FORMAT);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(dateString.trim(), DISPLAY_FORMAT);
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Unable to parse date: " + dateString, e2);
            }
        }
    }

    public static boolean isFutureDate(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    public static boolean isPastDate(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }
}
