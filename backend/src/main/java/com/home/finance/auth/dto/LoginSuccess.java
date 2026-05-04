package com.home.finance.auth.dto;

public record LoginSuccess(
        AuthResponse response,
        String tokenValue
) {
}
