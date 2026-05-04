package com.home.finance.user.controller;

import com.home.finance.user.dto.UpdateUserRequest;
import com.home.finance.user.dto.UserMapper;
import com.home.finance.user.dto.UserResponse;
import com.home.finance.user.model.User;
import com.home.finance.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getCurrentUser(email);
        UserResponse response = userMapper.toResponse(user);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateUserRequest request,
                                                      Authentication authentication) {

        String email = authentication.getName();

        User updatedUser = userService.updateUser(email, request);

        UserResponse response = userMapper.toResponse(updatedUser);

        return ResponseEntity.ok(response);
    }
}
