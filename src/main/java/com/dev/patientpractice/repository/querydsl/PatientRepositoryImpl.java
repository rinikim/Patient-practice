package com.dev.patientpractice.repository.querydsl;

import com.dev.patientpractice.dto.request.patient.PatientsInquiryRequest;
import com.dev.patientpractice.dto.response.patient.PatientsInquiryResponse;
import com.dev.patientpractice.exception.ErrorCode;
import com.dev.patientpractice.exception.PatientApplicationException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.dev.patientpractice.entity.QPatient.patient;
import static com.dev.patientpractice.entity.QVisit.visit;

@Repository
@RequiredArgsConstructor
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<PatientsInquiryResponse> findAllByConditions(int pageNo, int pageSize, PatientsInquiryRequest params) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        BooleanBuilder builder = setBuilder(params);

        List<PatientsInquiryResponse> results = factory
                .select(Projections.constructor(PatientsInquiryResponse.class,
                        patient.name.as("name"),
                        patient.registrationNumber.as("registrationNumber"),
                        patient.genderCode.as("genderCode"),
                        patient.birthDate.as("birthDate"),
                        patient.phoneNumber.as("phoneNumber"),
                        visit.receivedAt.max().as("latestVisit")))
                .from(patient)
                .leftJoin(patient.visits, visit)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(patient.id.desc())
                .groupBy(patient.id)
                .fetch();

        // TODO 페이지관련 데이터 처리

        if (results.isEmpty()) {
            throw new PatientApplicationException(ErrorCode.PATIENT_NOT_FOUND);
        }

        return results;
    }

    public BooleanBuilder setBuilder(PatientsInquiryRequest params) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(params.getName())) {
            builder.and(patient.name.eq(params.getName()));
        }

        if (Objects.nonNull(params.getBirthDate())) {
            builder.and(patient.birthDate.eq(params.getBirthDate().toString()));
        }

        if (StringUtils.hasText(params.getRegistrationNumber())) {
            builder.and(patient.registrationNumber.eq(params.getRegistrationNumber()));
        }

        return builder;
    }
}
