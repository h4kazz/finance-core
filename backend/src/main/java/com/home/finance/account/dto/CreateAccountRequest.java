package com.home.finance.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "Account number is required")
        @Size(min = 10,  max = 10, message = "Account number length is 10 symbols")
        String accountNumber,

        @NotNull
        @PositiveOrZero(message = "Balance must be positive or zero")
        BigDecimal balance
) {
}
