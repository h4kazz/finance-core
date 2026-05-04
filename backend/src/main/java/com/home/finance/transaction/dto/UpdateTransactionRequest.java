package com.home.finance.transaction.dto;

import com.home.finance.category.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateTransactionRequest(
        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        TransactionType transactionType,

        @NotNull
        LocalDate date,

        String description,

        @NotNull
        Long accountId,

        @NotNull
        Long categoryId
) {
}
