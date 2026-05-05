package com.home.finance.AuthTest;

import com.home.finance.auth.model.JwtToken;
import com.home.finance.auth.service.JwtTokenService;
import com.home.finance.user.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Test
    void getAccessTokenTill_ReturnsOneHour() {
        Duration result = jwtTokenService.getAccessTokenTill();

        assertEquals(Duration.ofHours(1), result);
    }

    @Test
    void createToken_WhenValid_ReturnsTokenWithExpiry() {
        Jwt jwt = new Jwt(
                "token-value",
                Instant.parse("2026-05-05T09:00:00Z"),
                Instant.parse("2026-05-05T10:00:00Z"),
                Map.of("alg", "HS256"),
                Map.of("sub", "user@test.com")
        );
        ArgumentCaptor<JwtEncoderParameters> paramsCaptor = ArgumentCaptor.forClass(JwtEncoderParameters.class);

        when(jwtEncoder.encode(paramsCaptor.capture())).thenReturn(jwt);

        JwtToken token = jwtTokenService.createToken("user@test.com", Set.of(UserRole.USER));

        assertEquals("token-value", token.value());
        assertNotNull(token.expiresAt());
        assertTrue(token.expiresAt().isAfter(Instant.now().minusSeconds(1)));
    }
}
