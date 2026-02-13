package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV utility for file I/O persistence.
 * Demonstrates: File I/O, try-with-resources, String.split(",") parsing,
 * serialization/deserialization to CSV format.
 */
public final class CSVUtil {

    private CSVUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    // --- Ensure data directory exists ---
    private static void ensureDirectoryExists(String filePath) throws IOException {
        Path parent = Paths.get(filePath).getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    // ==================== DOCTORS ====================

    public static void saveDoctors(List<Doctor> doctors, String filePath) throws IOException {
        ensureDirectoryExists(filePath);
        // try-with-resources — auto-closes writer
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,name,age,gender,phone,email,specialization,consultationFee,yearsOfExperience");
            writer.newLine();
            for (Doctor d : doctors) {
                writer.write(String.join(",",
                        d.getId(), d.getName(), String.valueOf(d.getAge()),
                        d.getGender(), d.getPhone(), d.getEmail(),
                        d.getSpecialization().name(),
                        String.valueOf(d.getConsultationFee()),
                        String.valueOf(d.getYearsOfExperience())));
                writer.newLine();
            }
        }
    }

    public static List<Doctor> loadDoctors(String filePath) throws IOException {
        List<Doctor> doctors = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return doctors;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    Doctor d = new Doctor(
                            parts[0].trim(), parts[1].trim(),
                            Integer.parseInt(parts[2].trim()),
                            parts[3].trim(), parts[4].trim(), parts[5].trim(),
                            Specialization.valueOf(parts[6].trim()),
                            Double.parseDouble(parts[7].trim()),
                            Integer.parseInt(parts[8].trim())
                    );
                    doctors.add(d);
                }
            }
        }
        return doctors;
    }

    // ==================== PATIENTS ====================

    public static void savePatients(List<Patient> patients, String filePath) throws IOException {
        ensureDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,name,age,gender,phone,email,bloodGroup");
            writer.newLine();
            for (Patient p : patients) {
                writer.write(String.join(",",
                        p.getId(), p.getName(), String.valueOf(p.getAge()),
                        p.getGender(), p.getPhone(), p.getEmail(),
                        p.getBloodGroup()));
                writer.newLine();
            }
        }
    }

    public static List<Patient> loadPatients(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return patients;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Patient p = new Patient(
                            parts[0].trim(), parts[1].trim(),
                            Integer.parseInt(parts[2].trim()),
                            parts[3].trim(), parts[4].trim(), parts[5].trim(),
                            parts[6].trim()
                    );
                    patients.add(p);
                }
            }
        }
        return patients;
    }

    // ==================== APPOINTMENTS ====================

    public static void saveAppointments(List<Appointment> appointments, String filePath) throws IOException {
        ensureDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,doctorId,patientId,doctorName,patientName,dateTime,status,notes");
            writer.newLine();
            for (Appointment a : appointments) {
                writer.write(String.join(",",
                        a.getId(), a.getDoctorId(), a.getPatientId(),
                        a.getDoctorName(), a.getPatientName(),
                        DateUtil.formatForStorage(a.getAppointmentDateTime()),
                        a.getStatus().name(),
                        a.getNotes() != null ? a.getNotes() : ""));
                writer.newLine();
            }
        }
    }

    public static List<Appointment> loadAppointments(String filePath) throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return appointments;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Appointment a = new Appointment(
                            parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim(),
                            DateUtil.parseFromStorage(parts[5].trim()),
                            parts.length > 7 ? parts[7].trim() : ""
                    );
                    a.setStatus(AppointmentStatus.valueOf(parts[6].trim()));
                    appointments.add(a);
                }
            }
        }
        return appointments;
    }
}
