package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    Page<Visit> findAllByPatient_Id(Long patientId, Pageable pageable);
}
