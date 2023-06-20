package com.dev.patientpractice.dto.request.visit;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class VisitModificationRequest {
    private LocalDateTime receivedAt;  // 접수일시
    private String visitStatusCode;  // 방문상태코드
}
