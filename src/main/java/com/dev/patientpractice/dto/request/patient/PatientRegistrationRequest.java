package com.dev.patientpractice.dto.request.patient;

import com.dev.patientpractice.entity.Hospital;
import com.dev.patientpractice.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class PatientRegistrationRequest {
    @NotNull(message = "필수값 hospitalId 값이 없습니다.")
    private Long hospitalId;  // 병원 ID
    @NotNull(message = "필수값 name 값이 없습니다.")
    private String name;  // 환자명
    @NotNull(message = "필수값 genderCode 값이 없습니다.")
    private String genderCode;  // 성별코드
    private LocalDate birthDate;  // 생년월일
    private String phoneNumber;  // 휴대전화번호

    public Patient toEntity(Hospital hospital, String patientNumber) {
        return Patient.builder()
                .hospital(hospital)
                .name(name)
                .registrationNumber(patientNumber)
                .genderCode(genderCode)
                .birthDate(Objects.nonNull(birthDate) ? birthDate.toString() : null)
                .phoneNumber(Objects.nonNull(phoneNumber) ? phoneNumber : null)
                .build();
    }
}
