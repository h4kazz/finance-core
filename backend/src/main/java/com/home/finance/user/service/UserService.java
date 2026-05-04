package com.home.finance.user.service;

import com.home.finance.user.dto.UpdateUserRequest;
import com.home.finance.user.model.User;

import java.util.List;

public interface UserService {
    User getCurrentUser(String email);

    User updateUser(String email, UpdateUserRequest request);

    List<User> getAll();
}
