package com.dev.patientpractice.dto.response.patient;

import com.dev.patientpractice.entity.Hospital;
import com.dev.patientpractice.entity.Patient;
import com.dev.patientpractice.entity.Visit;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientInquiryResponse {

    private Long patientId;  // 환자 ID
    private String name;  // 환자명
    private String registrationNumber;  // 환자등록번호
    private String genderCode;  // 성별코드
    private String birthDate;  // 생년월일
    private String phoneNumber;  // 휴대전화번호
    private HospitalResponse hospital;  // 병원
    private List<VisitResponse> visits = new ArrayList<>();  // 환자방문

    public static PatientInquiryResponse from(Patient patient) {
        return PatientInquiryResponse.builder()
                .patientId(patient.getId())
                .name(patient.getName())
                .registrationNumber(patient.getRegistrationNumber())
                .genderCode(patient.getGenderCode())
                .birthDate(Objects.nonNull(patient.getBirthDate()) ? patient.getBirthDate().toString() : null)
                .phoneNumber(Objects.nonNull(patient.getPhoneNumber()) ? patient.getPhoneNumber().toString() : null)
                .hospital(HospitalResponse.from(patient.getHospital()))
                .visits(patient.getVisits().stream()
                        .map(VisitResponse::from)
                        .toList())
                .build();
    }

    @ToString
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HospitalResponse {
        private Long id;  // 병원 ID
        private String name;  // 병원명
        private String careFacilityNumber;  // 요양기관번호
        private String directorName;  // 병원장명

        public static HospitalResponse from(Hospital hospital) {
            return HospitalResponse.builder()
                    .id(hospital.getId())
                    .name(hospital.getName())
                    .careFacilityNumber(hospital.getCareFacilityNumber())
                    .directorName(hospital.getDirectorName())
                    .build();
        }
    }

    @ToString
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VisitResponse {
        private Long id;  // 환자방문 ID
        private LocalDateTime receivedAt;  // 접수일시
        private String visitStatusCode;  // 방문상태코드

        public static VisitResponse from(Visit visit) {
            return VisitResponse.builder()
                    .id(visit.getId())
                    .receivedAt(visit.getReceivedAt())
                    .visitStatusCode(visit.getVisitStatusCode())
                    .build();
        }
    }

}
