package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT COUNT(p) FROM Patient p WHERE YEAR(p.createdAt) = :year")
    int countByCreatedAtYear(int year);

    Optional<Patient> findByIdAndDeleted(Long patientId, boolean deleted);
}
