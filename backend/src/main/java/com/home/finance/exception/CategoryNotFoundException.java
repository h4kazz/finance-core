package com.home.finance.exception;

public class CategoryNotFoundException extends DefaultNotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category not found by ID: " + id);
    }
}
