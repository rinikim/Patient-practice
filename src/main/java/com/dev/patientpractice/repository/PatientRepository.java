package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Patient;
import com.dev.patientpractice.repository.querydsl.PatientRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.hospital.id = :hospitalId AND YEAR(p.createdAt) = :year")
    int countPatientsByYearAndHospitalId(@Param("hospitalId") Long hospitalId, @Param("year") int year);

    Optional<Patient> findByIdAndDeleted(Long patientId, boolean deleted);

}
