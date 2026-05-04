package com.home.finance.user.dto;

import com.home.finance.user.model.User;
import com.home.finance.user.model.UserRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleNames(user.getRoles())
        );
    }

    public void updateUserFromRequest(UpdateUserRequest request, User user) {
        user.setName(request.name());
    }

    private List<String> roleNames(Set<UserRole> roles) {
        return roles.stream()
                .map(Enum::name)
                .sorted()
                .toList();
    }

    public List<UserResponse> toResponseList(List<User> users) {
        List<UserResponse> response = new ArrayList<>();

        for (User user : users) {
            response.add(toResponse(user));
        }

        return response;
    }
}
