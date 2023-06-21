package com.dev.patientpractice.dto.response.visit;

import com.dev.patientpractice.dto.response.PageResponse;
import com.dev.patientpractice.entity.Visit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class VisitInquiryResponse {

    private List<VisitInquiry> visits;  // 환자방문 데이터
    private PageResponse page;  // 페이지

    public static VisitInquiryResponse of(List<Visit> visits, Pageable pageable, long totalCount) {
        return VisitInquiryResponse.builder()
                .visits(visits.stream()
                        .map(VisitInquiry::of)
                        .toList())
                .page(PageResponse.of(pageable, totalCount))
                .build();
    }


}
