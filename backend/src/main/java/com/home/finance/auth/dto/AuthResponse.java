package com.home.finance.auth.dto;

import java.time.Instant;
import java.util.List;

public record AuthResponse(
        String message,
        Instant expiresAt,
        String email,
        List<String> roles
) {
}
