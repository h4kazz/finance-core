package com.home.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DefaultNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(DefaultNotFoundException e) {
        return buildError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DefaultDuplicateException.class)
    public ResponseEntity<ApiError> handleDuplicate(DefaultDuplicateException e) {
        return buildError(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException e) {
        return buildError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(CategoryInUseException.class)
    public ResponseEntity<ApiError> handleCategoryInUse(CategoryInUseException e) {

        HttpStatus status = HttpStatus.CONFLICT;
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidTransactionCategoryException.class)
    public ResponseEntity<ApiError> handleInvalidTransactionCategory(InvalidTransactionCategoryException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ApiError> handleInvalidTransaction(InvalidTransactionException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), "Validation failed", fieldErrors);

        return ResponseEntity.status(status).body(error);
    }

    private ResponseEntity<ApiError> buildError(HttpStatus status, String message) {
        ApiError error = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message
        );

        return ResponseEntity.status(status).body(error);
    }
}
