package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByIdAndDeleted(Long id, boolean deleted);
}
