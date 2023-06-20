package com.dev.patientpractice.service;

import com.dev.patientpractice.dto.request.VisitCreationRequest;
import com.dev.patientpractice.entity.Hospital;
import com.dev.patientpractice.entity.Patient;
import com.dev.patientpractice.entity.Visit;
import com.dev.patientpractice.exception.ErrorCode;
import com.dev.patientpractice.exception.PatientApplicationException;
import com.dev.patientpractice.repository.HospitalRepository;
import com.dev.patientpractice.repository.PatientRepository;
import com.dev.patientpractice.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final CodeService codeService;
    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    @Transactional
    public void createVisit(VisitCreationRequest body) {
        codeService.checkCode("방문상태코드", body.getVisitStatusCode());

        Hospital hospital = hospitalRepository.findByIdAndDeleted(body.getHospitalId(), false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.HOSPITAL_NOT_FOUND, String.format("병원 ID: %s", body.getHospitalId())));

        Patient patient = patientRepository.findByIdAndDeleted(body.getPatientId(), false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.PATIENT_NOT_FOUND, String.format("환자 ID: %s", body.getPatientId())));

        Visit visit = body.toEntity(hospital, patient);
        visitRepository.save(visit);
    }
}
