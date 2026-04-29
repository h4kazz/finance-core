package com.home.finance.account.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAccountRequest(
        @NotBlank
        String accountNumber
) {
}
