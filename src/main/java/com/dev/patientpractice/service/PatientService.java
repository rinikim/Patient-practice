package com.dev.patientpractice.service;

import com.dev.patientpractice.dto.request.patient.PatientModificationRequest;
import com.dev.patientpractice.dto.request.patient.PatientRegistrationRequest;
import com.dev.patientpractice.dto.request.patient.PatientsInquiryRequest;
import com.dev.patientpractice.dto.response.patient.PatientInquiryResponse;
import com.dev.patientpractice.dto.response.patient.PatientsInquiryResponse;
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

    public static final String GENDER_CODE = "성별코드";
    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;
    private final CodeService codeService;

    @Transactional
    public void generatePatient(PatientRegistrationRequest body) {
        codeService.checkCode(GENDER_CODE, body.getGenderCode());

        Hospital hospital = hospitalRepository.findByIdAndDeleted(body.getHospitalId(), false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.HOSPITAL_NOT_FOUND, String.format("병원 ID: %s", body.getHospitalId())));

        String patientNumber = generatePatientRegistrationNumber(body.getHospitalId());
        Patient patient = body.toEntity(hospital, patientNumber);
        patientRepository.save(patient);
    }

    public String generatePatientRegistrationNumber(Long hospitalId) {
        int currentYear = LocalDateTime.now(ZoneId.of("Asia/Seoul")).getYear();
        int yearlySerialNumber = getSerialNumbersByYear(hospitalId, currentYear);
        String formattedSerialNumber = String.format("%06d", yearlySerialNumber);
        return currentYear + formattedSerialNumber;
    }

    public int getSerialNumbersByYear(Long hospitalId, int currentYear) {
        int sequentialNumber = patientRepository.countPatientsByYearAndHospitalId(hospitalId, currentYear);
        return sequentialNumber + 1;
    }

    @Transactional
    public void updatePatient(Long patientId, PatientModificationRequest body) {
        codeService.checkCode(GENDER_CODE, body.getGenderCode());

        Patient patient = patientRepository.findByIdAndDeleted(patientId, false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.PATIENT_NOT_FOUND, String.format("환자 ID: %s", patientId)));
        patient.update(body);
    }

    @Transactional
    public void deletePatient(Long patientId) {
        Patient patient = patientRepository.findByIdAndDeleted(patientId, false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.PATIENT_NOT_FOUND, String.format("환자 ID: %s", patientId)));
        patient.delete();
    }

    @Transactional(readOnly = true)
    public PatientInquiryResponse getPatient(Long patientId) {
        Patient patient = patientRepository.findByIdAndDeleted(patientId, false)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.PATIENT_NOT_FOUND, String.format("환자 ID: %s", patientId)));
        return PatientInquiryResponse.from(patient);
    }

    @Transactional(readOnly = true)
    public PatientsInquiryResponse getPatients(PatientsInquiryRequest params) {
        return patientRepository.findAllByConditions(params);
    }
}
