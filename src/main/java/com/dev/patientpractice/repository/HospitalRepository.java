package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
