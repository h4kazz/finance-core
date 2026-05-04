package com.home.finance.auth.model;

import java.time.Instant;

public record JwtToken(
        String value,
        Instant expiresAt
) {

}
