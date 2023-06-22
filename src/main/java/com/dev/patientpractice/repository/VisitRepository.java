package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    Page<Visit> findAllByPatient_Id(Long patientId, Pageable pageable);
    Optional<Visit> findByIdAndVisitStatusCodeIn(Long visitId, List<String> visitStatusCodes);
}
