package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.observer.AppointmentObserver;
import com.airtribe.meditrack.repository.AppointmentRepository;
import com.airtribe.meditrack.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing appointments.
 * Demonstrates: Observer pattern (notifies observers on changes),
 * JPA repository, enum usage, streams & lambdas, exception handling.
 */
@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final List<AppointmentObserver> observers;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorService doctorService, PatientService patientService,
                              List<AppointmentObserver> observers) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.observers = observers != null ? observers : new ArrayList<>();
    }

    // --- Observer management ---

    public void addObserver(AppointmentObserver observer) {
        observers.add(observer);
    }

    private void notifyCreated(Appointment appointment) {
        observers.forEach(o -> o.onAppointmentCreated(appointment));
    }

    private void notifyCancelled(Appointment appointment) {
        observers.forEach(o -> o.onAppointmentCancelled(appointment));
    }

    private void notifyStatusChanged(Appointment appointment) {
        observers.forEach(o -> o.onAppointmentStatusChanged(appointment));
    }

    // --- CRUD ---

    public Appointment createAppointment(String doctorId, String patientId,
                                         LocalDateTime dateTime, String notes) {
        Doctor doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new InvalidDataException("doctorId", "Doctor not found: " + doctorId));
        Patient patient = patientService.getPatientById(patientId)
                .orElseThrow(() -> new InvalidDataException("patientId", "Patient not found: " + patientId));

        Appointment appointment = new Appointment(
                IdGenerator.getInstance().nextAppointmentId(),
                doctorId, patientId,
                doctor.getName(), patient.getName(),
                dateTime, notes
        );
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment saved = appointmentRepository.save(appointment);

        notifyCreated(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public Appointment getAppointmentById(String id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment cancelAppointment(String id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);

        notifyCancelled(saved);
        return saved;
    }

    public Appointment updateStatus(String id, AppointmentStatus status) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        Appointment saved = appointmentRepository.save(appointment);

        notifyStatusChanged(saved);
        return saved;
    }

    // --- Search & Filter ---

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository
                .findByAppointmentDateTimeAfterAndStatusNotOrderByAppointmentDateTimeAsc(
                        LocalDateTime.now(), AppointmentStatus.CANCELLED);
    }

    // --- Analytics using streams ---

    /**
     * Count appointments per doctor.
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getAppointmentsCountPerDoctor() {
        return appointmentRepository.findAll().stream()
                .collect(Collectors.groupingBy(Appointment::getDoctorName, Collectors.counting()));
    }

    /**
     * Count appointments per status.
     */
    @Transactional(readOnly = true)
    public Map<AppointmentStatus, Long> getAppointmentsCountPerStatus() {
        return appointmentRepository.findAll().stream()
                .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));
    }

    public AppointmentRepository getRepository() {
        return appointmentRepository;
    }
}
