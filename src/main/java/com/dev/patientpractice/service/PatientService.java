package com.dev.patientpractice.service;

import com.dev.patientpractice.dto.request.PatientModificationRequest;
import com.dev.patientpractice.dto.request.PatientRegistrationRequest;
import com.dev.patientpractice.entity.Hospital;
import com.dev.patientpractice.entity.Patient;
import com.dev.patientpractice.exception.ErrorCode;
import com.dev.patientpractice.exception.PatientApplicationException;
import com.dev.patientpractice.repository.HospitalRepository;
import com.dev.patientpractice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public void generatePatient(PatientRegistrationRequest body) {
        String patientNumber = generatePatientRegistrationNumber();

        Hospital hospital = hospitalRepository.findByIdAndDeleted(body.getHospitalId(), false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.HOSPITAL_NOT_FOUND, String.format("병원 ID: %s", body.getHospitalId())));

        Patient patient = body.toEntity(hospital, patientNumber);
        patientRepository.save(patient);
    }

    public String generatePatientRegistrationNumber() {
        int currentYear = LocalDateTime.now(ZoneId.of("Asia/Seoul")).getYear();
        int yearlySerialNumber = GetSerialNumbersByYear(currentYear);
        String formattedSerialNumber = String.format("%06d", yearlySerialNumber);
        return currentYear + formattedSerialNumber;
    }

    private int GetSerialNumbersByYear(int currentYear) {
        int sequentialNumber = patientRepository.countByCreatedAtYear(currentYear);
        return sequentialNumber + 1;
    }

    @Transactional
    public void updatePatient(Long patientId, PatientModificationRequest body) {
        Patient patient = patientRepository.findByIdAndDeleted(patientId, false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.PATIENT_NOT_FOUND, String.format("환자 ID: %s", patientId)));
        patient.update(body);
    }
}
