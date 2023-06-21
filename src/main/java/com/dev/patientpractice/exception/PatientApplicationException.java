package com.dev.patientpractice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public PatientApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        } else {
            return String.format("%s. %s", errorCode.getMessage(), message);
        }
    }
}
