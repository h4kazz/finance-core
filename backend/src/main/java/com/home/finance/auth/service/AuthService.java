package com.home.finance.auth.service;

import com.home.finance.auth.model.JwtToken;
import com.home.finance.auth.dto.LoginSuccess;
import com.home.finance.auth.dto.AuthResponse;
import com.home.finance.auth.dto.LoginRequest;
import com.home.finance.auth.dto.RegisterRequest;
import com.home.finance.exception.BadCredentialsException;
import com.home.finance.user.dto.UserResponse;
import com.home.finance.user.model.CustomUserDetails;
import com.home.finance.user.model.User;
import com.home.finance.user.model.UserRole;
import com.home.finance.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Set.of(UserRole.USER)
        );

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    public LoginSuccess login(LoginRequest request) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password", e);
        }

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.user();
        JwtToken token = jwtTokenService.createToken(user.getEmail(), user.getRoles());

        AuthResponse response = new AuthResponse("Login succesful", token.expiresAt(), user.getEmail(), roleNames(user));

        return new LoginSuccess(response, token.value());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), roleNames(user));
    }

    private static List<String> roleNames(User user) {
        return user.getRoles()
                .stream()
                .map(Enum::name)
                .sorted()
                .toList();
    }
}
