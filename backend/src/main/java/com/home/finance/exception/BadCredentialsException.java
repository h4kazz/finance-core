package com.home.finance.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
