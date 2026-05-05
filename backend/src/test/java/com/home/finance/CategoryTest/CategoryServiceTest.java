package com.home.finance.CategoryTest;

import com.home.finance.category.model.Category;
import com.home.finance.category.model.TransactionType;
import com.home.finance.category.repository.CategoryRepository;
import com.home.finance.category.service.DefaultCategoryService;
import com.home.finance.exception.CategoryInUseException;
import com.home.finance.exception.CategoryNotFoundException;
import com.home.finance.exception.DuplicateCategoryException;
import com.home.finance.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DefaultCategoryService categoryService;

    @Test
    void getAll_ReturnsAllCategories() {
        Category category = new Category();
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.getAll();

        assertEquals(1, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void getById_WhenCategoryExists_ReturnsCategory() {
        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getById(1L);

        assertEquals(1L, result.getId());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getById_WhenCategoryMissing_ThrowsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(1L));
    }

    @Test
    void create_WhenDuplicateNameAndType_ThrowsException() {
        Category category = new Category();
        category.setName("Food");
        category.setTransactionType(TransactionType.EXPENSE);

        when(categoryRepository.existsByNameAndTransactionType("Food", TransactionType.EXPENSE)).thenReturn(true);

        assertThrows(DuplicateCategoryException.class, () -> categoryService.create(category));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void create_WhenValid_SavesCategory() {
        Category category = new Category();
        category.setName("Salary");
        category.setTransactionType(TransactionType.INCOME);

        when(categoryRepository.existsByNameAndTransactionType("Salary", TransactionType.INCOME)).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);

        assertEquals("Salary", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void update_WhenDuplicateNameAndType_ThrowsException() {
        Category existing = new Category();
        existing.setId(1L);
        existing.setName("Old");
        existing.setTransactionType(TransactionType.EXPENSE);

        Category update = new Category();
        update.setName("Food");
        update.setTransactionType(TransactionType.EXPENSE);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsByNameAndTransactionTypeAndIdNot("Food", TransactionType.EXPENSE, 1L))
                .thenReturn(true);

        assertThrows(DuplicateCategoryException.class, () -> categoryService.update(1L, update));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void update_WhenValid_UpdatesAndSaves() {
        Category existing = new Category();
        existing.setId(1L);
        existing.setName("Old");
        existing.setTransactionType(TransactionType.EXPENSE);

        Category update = new Category();
        update.setName("New");
        update.setTransactionType(TransactionType.INCOME);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsByNameAndTransactionTypeAndIdNot("New", TransactionType.INCOME, 1L))
                .thenReturn(false);
        when(categoryRepository.save(existing)).thenReturn(existing);

        Category result = categoryService.update(1L, update);

        assertEquals("New", result.getName());
        assertEquals(TransactionType.INCOME, result.getTransactionType());
        verify(categoryRepository).save(existing);
    }

    @Test
    void delete_WhenCategoryHasTransactions_ThrowsException() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(true);

        assertThrows(CategoryInUseException.class, () -> categoryService.delete(1L));
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void delete_WhenDataIntegrityViolation_ThrowsCategoryInUseException() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(false);
        doThrow(DataIntegrityViolationException.class).when(categoryRepository).flush();

        assertThrows(CategoryInUseException.class, () -> categoryService.delete(1L));
    }

    @Test
    void delete_WhenValid_DeletesCategory() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(false);

        categoryService.delete(1L);

        verify(categoryRepository).delete(category);
        verify(categoryRepository).flush();
    }
}
