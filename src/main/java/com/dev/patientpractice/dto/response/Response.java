package com.dev.patientpractice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private String resultCode;
    private String message;
    private T result;

    public Response(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public Response(String resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public static Response<Void> error(String errorCode, String message) {
        return new Response<>(errorCode, message);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }
}
