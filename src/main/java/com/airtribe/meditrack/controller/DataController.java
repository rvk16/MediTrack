package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.MedicalEntity;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillingService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.CSVUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * REST controller for CSV data persistence (save/load) and system stats.
 * Demonstrates: File I/O, try-with-resources (in CSVUtil),
 * JPA repository saveAll for bulk load.
 */
@RestController
@RequestMapping("/api/data")
public class DataController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final BillingService billingService;

    public DataController(DoctorService doctorService, PatientService patientService,
                          AppointmentService appointmentService, BillingService billingService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.billingService = billingService;
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveAllData() {
        try {
            CSVUtil.saveDoctors(doctorService.getAllDoctors(), Constants.DOCTORS_CSV);
            CSVUtil.savePatients(patientService.getAllPatients(), Constants.PATIENTS_CSV);
            CSVUtil.saveAppointments(appointmentService.getAllAppointments(), Constants.APPOINTMENTS_CSV);
            return ResponseEntity.ok(Map.of("message", "All data saved successfully to CSV files"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to save data: " + e.getMessage()));
        }
    }

    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadAllData() {
        try {
            var doctors = CSVUtil.loadDoctors(Constants.DOCTORS_CSV);
            doctorService.getRepository().saveAll(doctors);

            var patients = CSVUtil.loadPatients(Constants.PATIENTS_CSV);
            patientService.getRepository().saveAll(patients);

            var appointments = CSVUtil.loadAppointments(Constants.APPOINTMENTS_CSV);
            appointmentService.getRepository().saveAll(appointments);

            return ResponseEntity.ok(Map.of("message",
                    "Data loaded — Doctors: " + doctors.size()
                            + ", Patients: " + patients.size()
                            + ", Appointments: " + appointments.size()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to load data: " + e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(Map.of(
                "totalEntitiesCreated", MedicalEntity.getTotalEntitiesCreated(),
                "doctors", doctorService.getAllDoctors().size(),
                "patients", patientService.getAllPatients().size(),
                "appointments", appointmentService.getAllAppointments().size(),
                "bills", billingService.getAllBills().size()
        ));
    }
}
