package com.dev.patientpractice.dto.request.patient;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PatientModificationRequest {
    private String name;  // 환자명
    private String genderCode;  // 성별코드
    private LocalDate birthDate;  // 생년월일
    private String phoneNumber;  // 휴대전화번호
}
