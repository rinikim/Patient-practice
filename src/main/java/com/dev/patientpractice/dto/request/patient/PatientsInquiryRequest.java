package com.dev.patientpractice.dto.request.patient;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PatientsInquiryRequest {
    private int pageNo = 1;  // 페이지 번호
    private int pageSize = 10;  // 페이지 사이즈

    @NotNull(message = "필수값 hospitalId 값이 없습니다.")
    private Long hospitalId;  // 병원 ID
    private String name;  // 환자명
    private String registrationNumber;  // 환자등록번호
    private LocalDate birthDate;  // 생년월일
}
