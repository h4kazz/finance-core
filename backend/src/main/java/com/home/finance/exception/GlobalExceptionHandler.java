package com.home.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFound(AccountNotFoundException e) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ApiError> handleDuplicateAccount(DuplicateAccountException e) {

        HttpStatus status = HttpStatus.CONFLICT;
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }
}
