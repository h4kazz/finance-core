package com.home.finance.exception;

public class TransactionNotFoundException extends DefaultNotFoundException {
    public TransactionNotFoundException(Long id) {
        super("Transaction not found by ID: " + id);
    }
}
