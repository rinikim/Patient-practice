package com.dev.patientpractice.repository.querydsl;

import com.dev.patientpractice.dto.request.patient.PatientInquiry;
import com.dev.patientpractice.dto.request.patient.PatientsInquiryRequest;
import com.dev.patientpractice.dto.response.patient.PatientsInquiryResponse;
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
    public PatientsInquiryResponse findAllByConditions(int pageNo, int pageSize, PatientsInquiryRequest params) {
        BooleanBuilder builder = setBuilderByConditions(params);
        long totalCount = getTotalCount(builder);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<PatientInquiry> results = findAllByConditions(pageable, builder);

        return PatientsInquiryResponse.of(results, pageable, totalCount);
    }

    public int getTotalCount(BooleanBuilder builder) {
        return factory.select(patient.id)
                .from(patient)
                .where(builder)
                .fetch().size();
    }

    public List<PatientInquiry> findAllByConditions(Pageable pageable, BooleanBuilder builder) {
        return factory
                .select(Projections.constructor(PatientInquiry.class,
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
    }

    public BooleanBuilder setBuilderByConditions(PatientsInquiryRequest params) {
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
