package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
