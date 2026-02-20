package com.airtribe.meditrack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Represents a medical appointment.
 * Demonstrates: Cloneable with deep copy (nested Doctor/Patient references),
 * enum usage (AppointmentStatus), composition.
 * JPA: @Entity persisted to 'appointments' table.
 */
@Entity
@Table(name = "appointments")
public class Appointment extends MedicalEntity implements Cloneable {

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "appointment_date_time")
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // No-arg constructor
    public Appointment() {
        super();
        this.status = AppointmentStatus.PENDING;
    }

    // Parameterized constructor — chains to MedicalEntity(id)
    public Appointment(String id, String doctorId, String patientId,
                       String doctorName, String patientName,
                       LocalDateTime appointmentDateTime, String notes) {
        super(id);
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.PENDING;
        this.notes = notes;
    }

    // --- Deep Clone ---
    @Override
    public Appointment clone() {
        try {
            Appointment cloned = (Appointment) super.clone();
            // LocalDateTime is immutable, no deep copy needed
            // Strings are immutable, no deep copy needed
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported for Appointment", e);
        }
    }

    // --- MedicalEntity abstract methods ---

    @Override
    public String getEntityType() {
        return "Appointment";
    }

    @Override
    public String getSummary() {
        return "Appointment: " + patientName + " with Dr. " + doctorName
                + " on " + appointmentDateTime + " [" + status + "]";
    }

    // --- Getters and setters ---

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Appointment{id='" + getId() + "', doctor='" + doctorName
                + "', patient='" + patientName + "', status=" + status + "}";
    }
}
