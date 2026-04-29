package com.home.finance.auth;

import com.home.finance.user.UserRole;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Service
public class JwtTokenService {

    private static final Duration ACCESS_TOKEN_TILL = Duration.ofHours(1);
    private final JwtEncoder jwtEncoder;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public Duration getAccessTokenTill() {
        return ACCESS_TOKEN_TILL;
    }

    public JwtToken createToken(String email, Set<UserRole> roles) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(ACCESS_TOKEN_TILL);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("finance-core")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(email)
                .claim("roles", roles.stream().map(Enum::name).sorted().toList())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new JwtToken(tokenValue, expiresAt);
    }
}
