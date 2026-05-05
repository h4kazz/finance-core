package com.home.finance.exception;

public class DuplicateCategoryException extends DefaultDuplicateException {
    public DuplicateCategoryException(String name) {
        super("Category already exist: " + name);
    }
}
