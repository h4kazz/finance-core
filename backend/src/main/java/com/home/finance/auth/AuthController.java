package com.home.finance.auth;

import com.home.finance.auth.dto.*;
import com.home.finance.config.AuthCookieProperties;
import com.home.finance.exception.UserNotFoundException;
import com.home.finance.user.User;
import com.home.finance.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;
    private final AuthCookieProperties authCookieProperties;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, JwtTokenService jwtTokenService,
                          AuthCookieProperties authCookieProperties, UserRepository userRepository) {
        this.authService = authService;
        this.jwtTokenService = jwtTokenService;
        this.authCookieProperties = authCookieProperties;
        this.userRepository = userRepository;
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

    @PostMapping("/me")
    public UserResponse me(Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));

        List<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replaceFirst("^ROLE_", ""))
                .sorted()
                .toList();

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), List.of(user.getRoles().toString()));
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
