package com.home.finance.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank
        String accountNumber,

        @NotNull
        BigDecimal balance
) {
}
