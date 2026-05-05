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

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = categoryMapper.toCategory(request);
        Category createdCategory = categoryService.create(category);
        CategoryResponse response = categoryMapper.toResponse(createdCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateCategoryRequest request) {
        Category category = categoryMapper.toCategory(id, request);
        Category updatedCategory = categoryService.update(id, category);
        CategoryResponse response = categoryMapper.toResponse(updatedCategory);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
