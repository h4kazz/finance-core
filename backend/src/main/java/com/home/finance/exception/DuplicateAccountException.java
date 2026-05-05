package com.home.finance.exception;

public class DuplicateAccountException extends DefaultDuplicateException {
    public DuplicateAccountException(String accountNumber) {
        super("Account number already exist: " + accountNumber);
    }
}
