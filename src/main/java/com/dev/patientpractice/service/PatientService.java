package com.dev.patientpractice.service;

import com.dev.patientpractice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

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
}
