package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller for Appointment management.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Create a new appointment.
     * Request body: { "doctorId": "...", "patientId": "...", "dateTime": "2025-03-15T10:00:00", "notes": "..." }
     */
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Map<String, String> request) {
        String doctorId = request.get("doctorId");
        String patientId = request.get("patientId");
        LocalDateTime dateTime = LocalDateTime.parse(request.get("dateTime"));
        String notes = request.getOrDefault("notes", "");

        Appointment appointment = appointmentService.createAppointment(doctorId, patientId, dateTime, notes);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable String id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable String id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(@PathVariable String id,
                                                     @RequestParam String status) {
        AppointmentStatus newStatus = AppointmentStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(appointmentService.updateStatus(id, newStatus));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getByDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Appointment>> getUpcoming() {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments());
    }

    @GetMapping("/analytics/per-doctor")
    public ResponseEntity<Map<String, Long>> getCountPerDoctor() {
        return ResponseEntity.ok(appointmentService.getAppointmentsCountPerDoctor());
    }

    @GetMapping("/analytics/per-status")
    public ResponseEntity<Map<AppointmentStatus, Long>> getCountPerStatus() {
        return ResponseEntity.ok(appointmentService.getAppointmentsCountPerStatus());
    }
}
