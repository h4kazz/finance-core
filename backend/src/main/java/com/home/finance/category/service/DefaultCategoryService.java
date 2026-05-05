package com.home.finance.category.service;

import com.home.finance.category.model.Category;
import com.home.finance.category.repository.CategoryRepository;
import com.home.finance.exception.CategoryInUseException;
import com.home.finance.exception.CategoryNotFoundException;
import com.home.finance.exception.DuplicateCategoryException;
import com.home.finance.transaction.repository.TransactionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public DefaultCategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category create(Category category) {
        if (categoryRepository.existsByNameAndTransactionType(category.getName(), category.getTransactionType())) {
            throw new DuplicateCategoryException(category.getName());
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category existingCategory = getById(id);

        if (categoryRepository.existsByNameAndTransactionTypeAndIdNot(category.getName(), category.getTransactionType(), id)) {
            throw new DuplicateCategoryException(category.getName());
        }

        existingCategory.setName(category.getName());
        existingCategory.setTransactionType(category.getTransactionType());

        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long id) {
        Category category = getById(id);

        if (transactionRepository.existsByCategoryId(id)) {
            throw new CategoryInUseException("Category is already used by transactions: " + id);
        }

        try {
            categoryRepository.delete(category);
            categoryRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new CategoryInUseException("Category is already used by transactions: " + id);
        }
    }
}
