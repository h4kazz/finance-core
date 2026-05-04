package com.home.finance.auth.controller;

import com.home.finance.auth.service.AuthService;
import com.home.finance.auth.service.JwtTokenService;
import com.home.finance.auth.dto.LoginSuccess;
import com.home.finance.auth.dto.AuthResponse;
import com.home.finance.auth.dto.LoginRequest;
import com.home.finance.auth.dto.LogoutResponse;
import com.home.finance.auth.dto.RegisterRequest;
import com.home.finance.config.AuthCookieProperties;
import com.home.finance.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;
    private final AuthCookieProperties authCookieProperties;

    public AuthController(AuthService authService, JwtTokenService jwtTokenService,
                          AuthCookieProperties authCookieProperties) {
        this.authService = authService;
        this.jwtTokenService = jwtTokenService;
        this.authCookieProperties = authCookieProperties;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginSuccess loginSuccess = authService.login(request);

        return ResponseEntity.ok()
                .header("Set-Cookie", createAuthCookie(loginSuccess.tokenValue()).toString())
                .body(loginSuccess.response());
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {
        return ResponseEntity.ok()
                .header("Set-Cookie", clearAuthCookie().toString())
                .body(new LogoutResponse("Logout successful"));
    }


    private ResponseCookie createAuthCookie(String tokenValue) {
        return ResponseCookie.from(authCookieProperties.getName(), tokenValue)
                .httpOnly(true)
                .secure(authCookieProperties.isSecure())
                .path(authCookieProperties.getPath())
                .sameSite(authCookieProperties.getSameSite())
                .maxAge(jwtTokenService.getAccessTokenTill())
                .build();
    }

    private ResponseCookie clearAuthCookie() {
        return ResponseCookie.from(authCookieProperties.getName(), "")
                .httpOnly(true)
                .secure(authCookieProperties.isSecure())
                .path(authCookieProperties.getPath())
                .sameSite(authCookieProperties.getSameSite())
                .maxAge(0)
                .build();
    }
}
