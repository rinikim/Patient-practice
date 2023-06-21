package com.dev.patientpractice.dto.request.patient;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PatientsInquiryRequest {
    private String name;  // 환자명
    private String registrationNumber;  // 환자등록번호
    private LocalDate birthDate;  // 생년월일
}
