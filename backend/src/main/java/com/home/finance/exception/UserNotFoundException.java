package com.home.finance.exception;

public class UserNotFoundException extends DefaultNotFoundException {
    public UserNotFoundException(String email) {
        super("User not found by email: " + email);
    }
}
