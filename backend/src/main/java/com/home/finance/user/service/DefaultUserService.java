package com.home.finance.user.service;

import com.home.finance.exception.UserNotFoundException;
import com.home.finance.user.dto.UpdateUserRequest;
import com.home.finance.user.dto.UserMapper;
import com.home.finance.user.repository.UserRepository;
import com.home.finance.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public DefaultUserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
    }

    @Override
    public User updateUser(String email, UpdateUserRequest request) {
        User existingUser = getCurrentUser(email);

        userMapper.updateUserFromRequest(request, existingUser);

        User savedUser = userRepository.save(existingUser);

        return userRepository.save(savedUser);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
