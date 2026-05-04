package com.home.finance.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank
        @Size(min = 4, max = 50, message = "Name must contain 4 to 50 characters")
        String name
) {
}
