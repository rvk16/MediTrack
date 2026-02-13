package com.airtribe.meditrack.repository;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Doctor entity.
 * Demonstrates: JpaRepository with custom query methods, @Query JPQL.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    List<Doctor> findBySpecialization(Specialization specialization);

    List<Doctor> findByNameContainingIgnoreCase(String name);

    List<Doctor> findByConsultationFeeLessThanEqual(double maxFee);

    List<Doctor> findByYearsOfExperienceGreaterThanEqual(int minYears);

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(d.specialization AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchByKeyword(@Param("keyword") String keyword);

    List<Doctor> findAllByOrderByConsultationFeeAsc();
}
