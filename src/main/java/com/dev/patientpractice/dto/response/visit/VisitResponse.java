package com.dev.patientpractice.dto.response.visit;

import com.dev.patientpractice.entity.Visit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class VisitResponse {
    private Long id;  // 환자방문ID
    private Long hospitalId;  // 병원ID
    private Long patientId;  // 환자ID
    private LocalDateTime receivedAt;  // 접수일시
    private String visitStatusCode;  // 방문상태코드

    public static VisitResponse from(Visit visit) {
        return VisitResponse.builder()
                .id(visit.getId())
                .hospitalId(visit.getHospital().getId())
                .patientId(visit.getPatient().getId())
                .receivedAt(visit.getReceivedAt())
                .visitStatusCode(visit.getVisitStatusCode())
                .build();
    }
}
