package com.airtribe.meditrack.repository;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for Appointment entity.
 * Demonstrates: derived query methods with enum parameters and date comparisons.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findByDoctorId(String doctorId);

    List<Appointment> findByPatientId(String patientId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByAppointmentDateTimeAfterAndStatusNotOrderByAppointmentDateTimeAsc(
            LocalDateTime dateTime, AppointmentStatus excludedStatus);
}
