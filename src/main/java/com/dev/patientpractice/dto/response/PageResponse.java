package com.dev.patientpractice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {
    private int page;  // 현재 페이지
    private int size;  // 현재 페이지에서 보이는 데이터 수
    private int totalPages;  // 전체 페이지 수
    private int totalCount;  // 전체 데이터 수

    public static PageResponse of(Pageable pageable, long totalCount) {
        return PageResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalPages((int) Math.ceil((double) totalCount / pageable.getPageSize()))
                .totalCount((int) totalCount)
                .build();

    }
}
