package com.home.finance.exception;

public class CategoryInUseException extends RuntimeException {
    public CategoryInUseException(Long id) {
        super("Category is already used by transactions: " + id);
    }
}
