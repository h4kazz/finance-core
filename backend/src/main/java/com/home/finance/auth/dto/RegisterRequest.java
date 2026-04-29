package com.home.finance.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Name is required!")
        @Size(min = 3, max = 50, message = "Name must containt 3 to 50 characters")
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank(message = "Password is required!")
        @Size(min = 4, max = 100, message = "Password must containt 4 to 100 characters")
        String password
) {
}
