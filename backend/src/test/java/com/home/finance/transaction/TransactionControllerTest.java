package com.home.finance.transaction;

import com.home.finance.account.model.Account;
import com.home.finance.category.model.Category;
import com.home.finance.category.model.TransactionType;
import com.home.finance.transaction.controller.TransactionController;
import com.home.finance.transaction.dto.CreateTransactionRequest;
import com.home.finance.transaction.dto.TransactionMapper;
import com.home.finance.transaction.dto.TransactionResponse;
import com.home.finance.transaction.dto.UpdateTransactionRequest;
import com.home.finance.transaction.model.Transaction;
import com.home.finance.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void create_WhenValid_ReturnsCreatedTransaction() {
        CreateTransactionRequest request = new CreateTransactionRequest(
                new BigDecimal("100.00"),
                TransactionType.INCOME,
                LocalDate.of(2026, 5, 1),
                "Salary",
                1L,
                2L
        );
        Transaction transaction = new Transaction();
        Transaction createdTransaction = buildTransaction(10L, new BigDecimal("100.00"), TransactionType.INCOME, "Salary");
        TransactionResponse response = buildResponse(10L, new BigDecimal("100.00"), TransactionType.INCOME, "Salary");

        when(authentication.getName()).thenReturn("user@test.com");
        when(transactionMapper.toTransaction(request)).thenReturn(transaction);
        when(transactionService.createTransactionForUser(transaction, 1L, 2L, "user@test.com"))
                .thenReturn(createdTransaction);
        when(transactionMapper.toResponse(createdTransaction)).thenReturn(response);

        ResponseEntity<TransactionResponse> result = transactionController.create(request, authentication);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(transactionService).createTransactionForUser(transaction, 1L, 2L, "user@test.com");
    }

    @Test
    void getById_WhenTransactionExists_ReturnsOk() {
        Transaction transaction = buildTransaction(11L, new BigDecimal("30.00"), TransactionType.EXPENSE, "Food");
        TransactionResponse response = buildResponse(11L, new BigDecimal("30.00"), TransactionType.EXPENSE, "Food");

        when(authentication.getName()).thenReturn("user@test.com");
        when(transactionService.getByIdForUser(11L, "user@test.com")).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction)).thenReturn(response);

        ResponseEntity<TransactionResponse> result = transactionController.getById(11L, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(transactionService).getByIdForUser(11L, "user@test.com");
    }

    @Test
    void update_WhenValid_ReturnsUpdatedTransaction() {
        UpdateTransactionRequest request = new UpdateTransactionRequest(
                new BigDecimal("50.00"),
                TransactionType.EXPENSE,
                LocalDate.of(2026, 5, 2),
                "Transport",
                3L,
                4L
        );
        Transaction mappedTransaction = new Transaction();
        Transaction updatedTransaction = buildTransaction(12L, new BigDecimal("50.00"), TransactionType.EXPENSE, "Transport");
        TransactionResponse response = buildResponse(12L, new BigDecimal("50.00"), TransactionType.EXPENSE, "Transport");

        when(authentication.getName()).thenReturn("user@test.com");
        when(transactionMapper.toTransaction(12L, request)).thenReturn(mappedTransaction);
        when(transactionService.updateTransactionForUser(12L, "user@test.com", mappedTransaction, 3L, 4L))
                .thenReturn(updatedTransaction);
        when(transactionMapper.toResponse(updatedTransaction)).thenReturn(response);

        ResponseEntity<TransactionResponse> result = transactionController.update(12L, request, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(transactionService).updateTransactionForUser(12L, "user@test.com", mappedTransaction, 3L, 4L);
    }

    @Test
    void delete_WhenValid_ReturnsNoContent() {
        when(authentication.getName()).thenReturn("user@test.com");

        ResponseEntity<Void> result = transactionController.delete(13L, authentication);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(transactionService).deleteForUser(13L, "user@test.com");
    }

    @Test
    void search_WhenFiltersProvided_ReturnsMappedList() {
        Transaction transaction = buildTransaction(14L, new BigDecimal("15.00"), TransactionType.EXPENSE, "Coffee");
        TransactionResponse response = buildResponse(14L, new BigDecimal("15.00"), TransactionType.EXPENSE, "Coffee");

        when(authentication.getName()).thenReturn("user@test.com");
        when(transactionService.search("user@test.com", TransactionType.EXPENSE, "Coffee"))
                .thenReturn(List.of(transaction));
        when(transactionMapper.toResponse(transaction)).thenReturn(response);

        ResponseEntity<List<TransactionResponse>> result = transactionController.search(
                TransactionType.EXPENSE,
                "Coffee",
                authentication
        );

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(response, result.getBody().get(0));
        verify(transactionService).search("user@test.com", TransactionType.EXPENSE, "Coffee");
    }

    private Transaction buildTransaction(Long id, BigDecimal amount, TransactionType type, String description) {
        Account account = new Account();
        account.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setName("Category");
        category.setTransactionType(type);

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setDate(LocalDate.of(2026, 5, 1));
        transaction.setDescription(description);
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setCreatedAt(Instant.parse("2026-05-01T10:00:00Z"));
        transaction.setUpdatedAt(Instant.parse("2026-05-01T10:00:00Z"));
        return transaction;
    }

    private TransactionResponse buildResponse(Long id, BigDecimal amount, TransactionType type, String description) {
        return new TransactionResponse(
                id,
                amount,
                type,
                LocalDate.of(2026, 5, 1),
                description,
                1L,
                2L,
                "Category",
                Instant.parse("2026-05-01T10:00:00Z"),
                Instant.parse("2026-05-01T10:00:00Z")
        );
    }
}
