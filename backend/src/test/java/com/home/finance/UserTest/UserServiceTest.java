package com.home.finance.UserTest;

import com.home.finance.exception.UserNotFoundException;
import com.home.finance.user.dto.UpdateUserRequest;
import com.home.finance.user.dto.UserMapper;
import com.home.finance.user.model.User;
import com.home.finance.user.repository.UserRepository;
import com.home.finance.user.service.DefaultUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private DefaultUserService userService;

    @Test
    void getCurrentUser_WhenExists_ReturnsUser() {
        User user = new User("Test", "user@test.com", "x", java.util.Set.of());
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser("user@test.com");

        assertEquals("user@test.com", result.getEmail());
    }

    @Test
    void getCurrentUser_WhenMissing_ThrowsException() {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser("user@test.com"));
    }

    @Test
    void updateUser_WhenValid_MapsAndSavesTwice() {
        User user = new User("Old Name", "user@test.com", "x", java.util.Set.of());
        UpdateUserRequest request = new UpdateUserRequest("New Name");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser("user@test.com", request);

        assertEquals(user, result);
        verify(userMapper).updateUserFromRequest(request, user);
        verify(userRepository, times(2)).save(user);
    }

    @Test
    void getAll_ReturnsUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User("a@test.com")));

        List<User> result = userService.getAll();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }
}
