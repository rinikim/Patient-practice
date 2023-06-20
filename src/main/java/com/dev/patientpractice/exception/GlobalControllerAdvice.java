package com.dev.patientpractice.exception;

import com.dev.patientpractice.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(PatientApplicationException.class)
    public ResponseEntity<?> applicationHandler(PatientApplicationException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error(e.getErrorCode().name(), e.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleMethodArgumentValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus())
                .body(Response.error(ErrorCode.INVALID_PARAMETER.name(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<?> handleMethodArgumentValidException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(ErrorCode.INVALID_PARAMETER.getHttpStatus())
                .body(Response.error(ErrorCode.INVALID_PARAMETER.name(), e.getCause().getCause().getLocalizedMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));

    }

}
