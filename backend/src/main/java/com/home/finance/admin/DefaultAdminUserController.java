package com.home.finance.admin;

import com.home.finance.user.dto.UserMapper;
import com.home.finance.user.dto.UserResponse;
import com.home.finance.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class DefaultAdminUserController {
    private final DefaultAdminService defaultAdminService;
    private final UserMapper userMapper;

    public DefaultAdminUserController(DefaultAdminService defaultAdminService, UserMapper userMapper) {
        this.defaultAdminService = defaultAdminService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserResponse> getAll() {
        List<User> users = defaultAdminService.getAllUsers();
        return userMapper.toResponseList(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        User user = defaultAdminService.getById(id);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }
}