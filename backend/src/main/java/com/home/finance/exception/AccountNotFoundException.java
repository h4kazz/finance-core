package com.home.finance.exception;

public class AccountNotFoundException extends DefaultNotFoundException {
    public AccountNotFoundException(Long id) {
        super("Account not found by ID: " + id);
    }
}
