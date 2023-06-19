package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
