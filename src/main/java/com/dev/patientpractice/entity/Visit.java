package com.dev.patientpractice.entity;

import com.dev.patientpractice.dto.request.visit.VisitModificationRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Visit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;  // 병원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;  // 환자

    @Column(nullable = false)
    private LocalDateTime receivedAt;  // 접수일시

    @Column(nullable = false, length = 10)
    private String visitStatusCode;  // 방문상태코드

    @Builder
    public Visit(Hospital hospital, Patient patient, LocalDateTime receivedAt, String visitStatusCode) {
        this.hospital = hospital;
        this.patient = patient;
        this.receivedAt = receivedAt;
        this.visitStatusCode = visitStatusCode;
    }

    /**
     * 코드테이블에서 관리하고 있는 방문상태코드 중 '3'은 '취소'를 의미한다.
     */
    public void delete() {
        this.visitStatusCode = "3";
    }

    public void update(VisitModificationRequest visit) {
        if (Objects.nonNull(visit.getReceivedAt())) {
            this.receivedAt = visit.getReceivedAt();
        }

        if (StringUtils.hasText(visit.getVisitStatusCode())) {
            this.visitStatusCode = visit.getVisitStatusCode();
        }
    }
}
