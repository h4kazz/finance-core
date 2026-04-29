package com.home.finance.auth;

import com.home.finance.auth.dto.AuthResponse;

public record LoginSuccess(
        AuthResponse response,
        String tokenValue
) {
}
