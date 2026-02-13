package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.repository.PatientRepository;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Patient CRUD and search operations.
 * Demonstrates: Spring @Service + @Transactional, JPA repository,
 * encapsulation, generics (DataStore kept for demo), overloaded search (polymorphism),
 * deep clone usage.
 */
@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    // DataStore kept as a generics demonstration
    private final DataStore<Patient> patientCache = new DataStore<>();

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // --- CRUD ---

    public Patient addPatient(Patient patient) {
        Validator.validateNotEmpty(patient.getName(), "name");
        Validator.validateAge(patient.getAge());

        if (patient.getId() == null || patient.getId().isEmpty()) {
            patient.setId(IdGenerator.getInstance().nextPatientId());
        }
        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getPatientById(String id) {
        return patientRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(String id, Patient updated) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("id", "Patient not found: " + id));

        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getAge() > 0) existing.setAge(updated.getAge());
        if (updated.getGender() != null) existing.setGender(updated.getGender());
        if (updated.getPhone() != null) existing.setPhone(updated.getPhone());
        if (updated.getEmail() != null) existing.setEmail(updated.getEmail());
        if (updated.getBloodGroup() != null) existing.setBloodGroup(updated.getBloodGroup());
        if (updated.getAllergies() != null) existing.setAllergies(updated.getAllergies());
        if (updated.getMedicalHistory() != null) existing.setMedicalHistory(updated.getMedicalHistory());

        return patientRepository.save(existing);
    }

    public boolean deletePatient(String id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Polymorphism: overloaded search methods ---

    /**
     * Search by keyword (name, id, blood group).
     */
    @Transactional(readOnly = true)
    public List<Patient> searchPatients(String keyword) {
        return patientRepository.searchByKeyword(keyword);
    }

    /**
     * Search by name (overloaded).
     */
    @Transactional(readOnly = true)
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Search by age (overloaded).
     */
    @Transactional(readOnly = true)
    public List<Patient> searchPatientsByAge(int age) {
        return patientRepository.findByAge(age);
    }

    /**
     * Demonstrate deep clone — returns a cloned copy of a patient.
     */
    public Patient clonePatient(String id) {
        Patient original = patientRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("id", "Patient not found: " + id));
        Patient cloned = original.clone();
        cloned.setId(IdGenerator.getInstance().nextPatientId());
        cloned.setName(original.getName() + " (Copy)");
        // Deep-copied lists need fresh ArrayList to avoid JPA managed collection issues
        cloned.setAllergies(new ArrayList<>(cloned.getAllergies()));
        cloned.setMedicalHistory(new ArrayList<>(cloned.getMedicalHistory()));
        return patientRepository.save(cloned);
    }

    public PatientRepository getRepository() {
        return patientRepository;
    }

    public DataStore<Patient> getCache() {
        return patientCache;
    }
}
