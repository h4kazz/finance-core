package com.home.finance.transaction.dto;

import com.home.finance.category.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        TransactionType transactionType,
        LocalDate date,
        String description,
        Long accountId,
        Long categoryId,
        String categoryName,
        Instant createdAt,
        Instant updatedAt
) {
}
