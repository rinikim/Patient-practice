package com.dev.patientpractice.dto.request;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class PatientModificationRequest {
    private String name;  // 환자명
    private String genderCode;  // 성별코드
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;  // 생년월일
    private String phoneNumber;  // 휴대전화번호
}
