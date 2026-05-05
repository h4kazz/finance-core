package com.home.finance.AuthTest;

import com.home.finance.auth.dto.LoginRequest;
import com.home.finance.auth.dto.LoginSuccess;
import com.home.finance.auth.dto.RegisterRequest;
import com.home.finance.auth.model.JwtToken;
import com.home.finance.auth.service.AuthService;
import com.home.finance.auth.service.JwtTokenService;
import com.home.finance.user.dto.UserResponse;
import com.home.finance.user.model.CustomUserDetails;
import com.home.finance.user.model.User;
import com.home.finance.user.model.UserRole;
import com.home.finance.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_WhenEmailAlreadyExists_ThrowsConflict() {
        RegisterRequest request = new RegisterRequest("Test", "user@test.com", "secret");
        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.register(request));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void register_WhenValid_ReturnsUserResponse() {
        RegisterRequest request = new RegisterRequest("Test", "user@test.com", "secret");
        User saved = new User("Test", "user@test.com", "encoded", Set.of(UserRole.USER));
        saved.setId(1L);

        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserResponse response = authService.register(request);

        assertEquals(1L, response.id());
        assertEquals("user@test.com", response.email());
        assertEquals(1, response.roles().size());
        assertEquals("USER", response.roles().get(0));
    }

    @Test
    void login_WhenAuthenticationFails_ThrowsRuntimeException() {
        LoginRequest request = new LoginRequest("user@test.com", "secret12");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    @Test
    void login_WhenValid_ReturnsTokenAndResponse() {
        LoginRequest request = new LoginRequest("user@test.com", "secret12");
        User user = new User("Test", "user@test.com", "x", Set.of(UserRole.USER));
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        JwtToken token = new JwtToken("jwt-token", Instant.parse("2026-05-05T10:00:00Z"));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(jwtTokenService.createToken("user@test.com", Set.of(UserRole.USER))).thenReturn(token);

        LoginSuccess result = authService.login(request);

        assertEquals("jwt-token", result.tokenValue());
        assertEquals("user@test.com", result.response().email());
        assertEquals("Login succesful", result.response().message());
    }
}
