package com.home.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

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

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFound(CategoryNotFoundException e) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<ApiError> handleDuplicateCategory(DuplicateCategoryException e) {

        HttpStatus status = HttpStatus.CONFLICT;
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(CategoryInUseException.class)
    public ResponseEntity<ApiError> handleCategoryInUse(CategoryInUseException e) {

        HttpStatus status = HttpStatus.CONFLICT;
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiError> handleTransactionNotFound(TransactionNotFoundException e) {

        HttpStatus status = HttpStatus.NOT_FOUND;
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException e) {

        HttpStatus status = HttpStatus.NOT_FOUND;
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
}
