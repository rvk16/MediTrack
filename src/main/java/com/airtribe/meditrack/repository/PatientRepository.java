package com.airtribe.meditrack.repository;

import com.airtribe.meditrack.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Patient entity.
 * Demonstrates: derived query methods, @Query with JPQL.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    List<Patient> findByNameContainingIgnoreCase(String name);

    List<Patient> findByAge(int age);

    List<Patient> findByBloodGroup(String bloodGroup);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.bloodGroup) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR p.id LIKE CONCAT('%', :keyword, '%')")
    List<Patient> searchByKeyword(@Param("keyword") String keyword);
}
