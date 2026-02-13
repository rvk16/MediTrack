package com.airtribe.meditrack.repository;

import com.airtribe.meditrack.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Bill entity.
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, String> {

    List<Bill> findByPatientId(String patientId);

    List<Bill> findByBillType(String billType);

    List<Bill> findByAppointmentId(String appointmentId);
}
