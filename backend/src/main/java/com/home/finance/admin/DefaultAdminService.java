package com.home.finance.admin;

import com.home.finance.exception.UserNotFoundException;
import com.home.finance.user.model.User;
import com.home.finance.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAdminService {
    private final UserRepository userRepository;

    public DefaultAdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }
}
