package com.dev.patientpractice.repository.querydsl;

import com.dev.patientpractice.dto.request.patient.PatientsInquiryRequest;
import com.dev.patientpractice.dto.response.patient.PatientsInquiryResponse;

import java.util.List;

public interface PatientRepositoryCustom {

    List<PatientsInquiryResponse> findAllByConditions(int pageNo, int pageSize, PatientsInquiryRequest params);
}
