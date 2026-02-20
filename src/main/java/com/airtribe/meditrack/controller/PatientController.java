package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.service.PatientService;
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

/**
 * REST controller for Patient CRUD, search, and clone demo.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        return new ResponseEntity<>(patientService.addPatient(patient), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        boolean deleted = patientService.deletePatient(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String keyword) {
        return ResponseEntity.ok(patientService.searchPatients(keyword));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Patient>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(patientService.searchPatientsByName(name));
    }

    @GetMapping("/search/age")
    public ResponseEntity<List<Patient>> searchByAge(@RequestParam int age) {
        return ResponseEntity.ok(patientService.searchPatientsByAge(age));
    }

    /**
     * Clone a patient — demonstrates deep copy.
     */
    @PostMapping("/{id}/clone")
    public ResponseEntity<Patient> clonePatient(@PathVariable String id) {
        return new ResponseEntity<>(patientService.clonePatient(id), HttpStatus.CREATED);
    }
}
