package com.home.finance.category.dto;

import com.home.finance.category.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {

    public Category toCategory(CreateCategoryRequest request) {
        Category category = new Category();

        category.setName(request.name());
        category.setTransactionType(request.transactionType());

        return category;
    }

    public Category toCategory(Long id, UpdateCategoryRequest request) {
        Category category = new Category();

        category.setId(id);
        category.setName(request.name());
        category.setTransactionType(request.transactionType());

        return category;
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getTransactionType(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        List<CategoryResponse> responses = new ArrayList<>();

        for (Category category : categories) {
            responses.add(toResponse(category));
        }

        return responses;
    }
}
