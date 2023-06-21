package com.dev.patientpractice.repository.querydsl;

import com.dev.patientpractice.dto.request.patient.PatientsInquiryRequest;
import com.dev.patientpractice.dto.response.patient.PatientsInquiryResponse;

public interface PatientRepositoryCustom {

    PatientsInquiryResponse findAllByConditions(PatientsInquiryRequest params);
}
