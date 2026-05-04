package com.home.finance.admin;

import com.home.finance.user.dto.UserMapper;
import com.home.finance.user.dto.UserResponse;
import com.home.finance.user.model.User;
import com.home.finance.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/api/users")
public class UserAdminController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserAdminController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(userMapper.toResponseList(users));
    }

}
