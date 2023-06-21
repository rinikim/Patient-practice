package com.dev.patientpractice.dto.response.patient;

import com.dev.patientpractice.dto.request.patient.PatientInquiry;
import com.dev.patientpractice.dto.response.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientsInquiryResponse {

    private List<PatientInquiry> patients;  // 환자 데이터
    private PageResponse page;  // 페이지 데이터

    public static PatientsInquiryResponse of(List<PatientInquiry> patients, Pageable pageable, long totalCount) {
        return new PatientsInquiryResponse(patients, PageResponse.of(pageable, totalCount));
    }
}
