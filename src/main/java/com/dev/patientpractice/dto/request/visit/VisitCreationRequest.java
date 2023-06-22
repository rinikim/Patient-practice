package com.dev.patientpractice.dto.request.visit;


import com.dev.patientpractice.entity.Hospital;
import com.dev.patientpractice.entity.Patient;
import com.dev.patientpractice.entity.Visit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitCreationRequest {
    @NotNull(message = "필수값 hospitalId 값이 없습니다.")
    private Long hospitalId;  // 병원 ID
    @NotNull(message = "필수값 patientId 값이 없습니다.")
    private Long patientId;  // 환자 ID
    @NotNull(message = "필수값 receivedAt 값이 없습니다.")
    private LocalDateTime receivedAt;  // 접수일시
    @NotNull(message = "필수값 visitStatusCode 값이 없습니다.")
    private String visitStatusCode;  // 방문상태코드

    public Visit toEntity(Hospital hospital, Patient patient) {
        return Visit.builder()
                .hospital(hospital)
                .patient(patient)
                .receivedAt(receivedAt)
                .visitStatusCode(visitStatusCode)
                .build();
    }

}
