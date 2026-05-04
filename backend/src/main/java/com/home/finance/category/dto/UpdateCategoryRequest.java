package com.home.finance.category.dto;

import com.home.finance.category.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCategoryRequest(
        @NotBlank
        String name,

        @NotNull
        TransactionType transactionType
) {
}
