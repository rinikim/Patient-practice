package com.dev.patientpractice.dto.response.visit;

import com.dev.patientpractice.entity.Visit;
import com.dev.patientpractice.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class VisitInquiry {
    private Long id;  // 환자방문ID
    private Long hospitalId;  // 병원ID
    private Long patientId;  // 환자ID
    private String receivedAt;  // 접수일시
    private String visitStatusCode;  // 방문상태코드

    public static VisitInquiry of(Visit visit) {
        return VisitInquiry.builder()
                .id(visit.getId())
                .hospitalId(visit.getHospital().getId())
                .patientId(visit.getPatient().getId())
                .receivedAt(DateUtils.formatLocalDateTime(visit.getReceivedAt(), "yyyy-MM-dd HH:mm:ss"))
                .visitStatusCode(visit.getVisitStatusCode())
                .build();
    }
}
