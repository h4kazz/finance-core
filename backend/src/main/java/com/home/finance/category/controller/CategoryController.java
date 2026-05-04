package com.home.finance.category.controller;

import com.home.finance.category.service.CategoryService;
import com.home.finance.category.dto.CategoryMapper;
import com.home.finance.category.dto.CategoryResponse;
import com.home.finance.category.dto.CreateCategoryRequest;
import com.home.finance.category.dto.UpdateCategoryRequest;
import com.home.finance.category.model.Category;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryService.getAll();
        return categoryMapper.toResponseList(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        CategoryResponse response = categoryMapper.toResponse(category);

        return ResponseEntity.ok(response);
    }
}
