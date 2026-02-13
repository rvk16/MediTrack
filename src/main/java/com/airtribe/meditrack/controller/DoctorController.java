package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Doctor CRUD and search.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        return new ResponseEntity<>(doctorService.addDoctor(doctor), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable String id) {
        return doctorService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable String id, @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable String id) {
        boolean deleted = doctorService.deleteDoctor(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam String keyword) {
        return ResponseEntity.ok(doctorService.searchDoctors(keyword));
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Doctor>> getBySpecialization(@PathVariable String specialization) {
        Specialization spec = Specialization.valueOf(specialization.toUpperCase());
        return ResponseEntity.ok(doctorService.searchDoctors(spec));
    }

    @GetMapping("/analytics/average-fee")
    public ResponseEntity<Map<String, Double>> getAverageFee() {
        return ResponseEntity.ok(Map.of("averageFee", doctorService.getAverageConsultationFee()));
    }

    @GetMapping("/analytics/by-specialization")
    public ResponseEntity<Map<Specialization, Long>> getCountBySpecialization() {
        return ResponseEntity.ok(doctorService.getDoctorCountBySpecialization());
    }

    @GetMapping("/sorted/fee")
    public ResponseEntity<List<Doctor>> getDoctorsSortedByFee() {
        return ResponseEntity.ok(doctorService.getDoctorsSortedByFee());
    }
}
