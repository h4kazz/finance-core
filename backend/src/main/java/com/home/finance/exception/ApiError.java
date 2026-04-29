package com.home.finance.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ApiError {
    private final int status;
    private final String error;
    private final String message;
    private List<FieldError> fieldErrors;

    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ApiError(int status, String error, String message, List<FieldError> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }
}
