package com.home.finance.exception;

public class InvalidTransactionCategoryException extends RuntimeException {
    public InvalidTransactionCategoryException(String message) {
        super(message);
    }
}
