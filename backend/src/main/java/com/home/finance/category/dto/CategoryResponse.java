package com.home.finance.category.dto;

import com.home.finance.category.model.TransactionType;

import java.time.Instant;

public record CategoryResponse(
        Long id,
        String name,
        TransactionType transactionType,
        Instant createdAt,
        Instant updatedAt
) {
}
