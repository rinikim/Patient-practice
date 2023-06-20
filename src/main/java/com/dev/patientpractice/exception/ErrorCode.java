package com.dev.patientpractice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 유효성 에러"),
    CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 코드 요청 에러"),
    PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 환자 요청 에러"),
    VISIT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 환자방문 요청 에러"),
    HOSPITAL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 병원 요청 에러"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 시스템 에러");

    private HttpStatus httpStatus;
    private String message;
}
