package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.repository.DoctorRepository;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for Doctor CRUD and search operations.
 * Demonstrates: Spring @Service + @Transactional, JPA repository usage,
 * generics (DataStore kept for demo), streams & lambdas,
 * polymorphism (overloaded search), Comparator usage.
 */
@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;

    // DataStore kept as a generics demonstration — not the primary store
    private final DataStore<Doctor> doctorCache = new DataStore<>();

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // --- CRUD ---

    public Doctor addDoctor(Doctor doctor) {
        Validator.validateNotEmpty(doctor.getName(), "name");
        Validator.validateAge(doctor.getAge());
        Validator.validateNotNull(doctor.getSpecialization(), "specialization");
        Validator.validatePositive(doctor.getConsultationFee(), "consultationFee");

        if (doctor.getId() == null || doctor.getId().isEmpty()) {
            doctor.setId(IdGenerator.getInstance().nextDoctorId());
        }
        return doctorRepository.save(doctor);
    }

    @Transactional(readOnly = true)
    public Optional<Doctor> getDoctorById(String id) {
        return doctorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor updateDoctor(String id, Doctor updated) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("id", "Doctor not found: " + id));

        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getAge() > 0) existing.setAge(updated.getAge());
        if (updated.getGender() != null) existing.setGender(updated.getGender());
        if (updated.getPhone() != null) existing.setPhone(updated.getPhone());
        if (updated.getEmail() != null) existing.setEmail(updated.getEmail());
        if (updated.getSpecialization() != null) existing.setSpecialization(updated.getSpecialization());
        if (updated.getConsultationFee() > 0) existing.setConsultationFee(updated.getConsultationFee());
        if (updated.getYearsOfExperience() > 0) existing.setYearsOfExperience(updated.getYearsOfExperience());

        return doctorRepository.save(existing);
    }

    public boolean deleteDoctor(String id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Polymorphism: overloaded search methods ---

    /**
     * Search doctors by keyword (name, specialization, id).
     */
    @Transactional(readOnly = true)
    public List<Doctor> searchDoctors(String keyword) {
        return doctorRepository.searchByKeyword(keyword);
    }

    /**
     * Search doctors by specialization enum (overloaded).
     */
    @Transactional(readOnly = true)
    public List<Doctor> searchDoctors(Specialization specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    // --- Streams & Lambdas analytics ---

    /**
     * Get average consultation fee using streams.
     */
    @Transactional(readOnly = true)
    public double getAverageConsultationFee() {
        return doctorRepository.findAll().stream()
                .mapToDouble(Doctor::getConsultationFee)
                .average()
                .orElse(0.0);
    }

    /**
     * Get doctors sorted by fee (ascending).
     */
    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsSortedByFee() {
        return doctorRepository.findAllByOrderByConsultationFeeAsc();
    }

    /**
     * Group doctors by specialization using streams.
     */
    @Transactional(readOnly = true)
    public Map<Specialization, List<Doctor>> getDoctorsGroupedBySpecialization() {
        return doctorRepository.findAll().stream()
                .collect(Collectors.groupingBy(Doctor::getSpecialization));
    }

    /**
     * Count doctors per specialization.
     */
    @Transactional(readOnly = true)
    public Map<Specialization, Long> getDoctorCountBySpecialization() {
        return doctorRepository.findAll().stream()
                .collect(Collectors.groupingBy(Doctor::getSpecialization, Collectors.counting()));
    }

    public DoctorRepository getRepository() {
        return doctorRepository;
    }

    public DataStore<Doctor> getCache() {
        return doctorCache;
    }
}
