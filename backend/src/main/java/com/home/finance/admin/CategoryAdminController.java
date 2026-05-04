package com.home.finance.admin;

import com.home.finance.category.dto.CategoryMapper;
import com.home.finance.category.dto.CategoryResponse;
import com.home.finance.category.dto.CreateCategoryRequest;
import com.home.finance.category.dto.UpdateCategoryRequest;
import com.home.finance.category.model.Category;
import com.home.finance.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/categories")
public class CategoryAdminController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    public CategoryAdminController(CategoryMapper categoryMapper, CategoryService categoryService) {
        this.categoryMapper = categoryMapper;
        this.categoryService = categoryService;
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
