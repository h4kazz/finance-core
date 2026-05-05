package com.home.finance.TransactionTest;

import com.home.finance.account.model.Account;
import com.home.finance.account.repository.AccountRepository;
import com.home.finance.category.model.Category;
import com.home.finance.category.model.TransactionType;
import com.home.finance.category.repository.CategoryRepository;
import com.home.finance.exception.AccountNotFoundException;
import com.home.finance.exception.CategoryNotFoundException;
import com.home.finance.exception.InvalidTransactionCategoryException;
import com.home.finance.exception.InvalidTransactionException;
import com.home.finance.exception.TransactionNotFoundException;
import com.home.finance.transaction.model.Transaction;
import com.home.finance.transaction.repository.TransactionRepository;
import com.home.finance.transaction.service.DefaultTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private DefaultTransactionService transactionService;

    @Test
    void getAllForUser_ReturnsTransactions() {
        when(transactionRepository.findByAccountUserEmail("user@test.com")).thenReturn(List.of(new Transaction()));

        List<Transaction> result = transactionService.getAllForUser("user@test.com");

        assertEquals(1, result.size());
    }

    @Test
    void getByIdForUser_WhenMissing_ThrowsException() {
        when(transactionRepository.findByIdAndAccountUserEmail(1L, "user@test.com")).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getByIdForUser(1L, "user@test.com"));
    }

    @Test
    void createTransactionForUser_WhenAccountIsNull_ThrowsException() {
        Transaction transaction = buildTransaction(TransactionType.INCOME, "10.00");

        assertThrows(InvalidTransactionException.class,
                () -> transactionService.createTransactionForUser(transaction, null, 2L, "user@test.com"));
    }

    @Test
    void createTransactionForUser_WhenCategoryIsNull_ThrowsException() {
        Transaction transaction = buildTransaction(TransactionType.INCOME, "10.00");
        Account account = new Account();
        when(accountRepository.findByIdAndUserEmail(1L, "user@test.com")).thenReturn(Optional.of(account));

        assertThrows(InvalidTransactionException.class,
                () -> transactionService.createTransactionForUser(transaction, 1L, null, "user@test.com"));
    }

    @Test
    void createTransactionForUser_WhenCategoryTypeMismatch_ThrowsException() {
        Transaction transaction = buildTransaction(TransactionType.INCOME, "10.00");
        Account account = new Account();
        Category category = new Category();
        category.setTransactionType(TransactionType.EXPENSE);

        when(accountRepository.findByIdAndUserEmail(1L, "user@test.com")).thenReturn(Optional.of(account));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        assertThrows(InvalidTransactionCategoryException.class,
                () -> transactionService.createTransactionForUser(transaction, 1L, 2L, "user@test.com"));
    }

    @Test
    void createTransactionForUser_WhenValidIncome_AddsBalanceAndSaves() {
        Transaction transaction = buildTransaction(TransactionType.INCOME, "100.00");
        Account account = new Account();
        account.setBalance(new BigDecimal("20.00"));
        Category category = new Category();
        category.setTransactionType(TransactionType.INCOME);

        when(accountRepository.findByIdAndUserEmail(1L, "user@test.com")).thenReturn(Optional.of(account));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.createTransactionForUser(transaction, 1L, 2L, "user@test.com");

        assertEquals(new BigDecimal("120.00"), account.getBalance());
        assertEquals(account, result.getAccount());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void updateTransactionForUser_WhenTransactionMissing_ThrowsException() {
        when(transactionRepository.findByIdAndAccountUserEmail(5L, "user@test.com")).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.updateTransactionForUser(5L, "user@test.com", new Transaction(), 1L, 2L));
    }

    @Test
    void updateTransactionForUser_WhenAccountMissing_ThrowsException() {
        Transaction existing = buildTransaction(TransactionType.EXPENSE, "20.00");
        existing.setAccount(new Account());

        when(transactionRepository.findByIdAndAccountUserEmail(5L, "user@test.com")).thenReturn(Optional.of(existing));
        when(accountRepository.findByIdAndUserEmail(1L, "user@test.com")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> transactionService.updateTransactionForUser(5L, "user@test.com", new Transaction(), 1L, 2L));
    }

    @Test
    void updateTransactionForUser_WhenCategoryMissing_ThrowsException() {
        Transaction existing = buildTransaction(TransactionType.EXPENSE, "20.00");
        existing.setAccount(new Account());
        Account account = new Account();

        when(transactionRepository.findByIdAndAccountUserEmail(5L, "user@test.com")).thenReturn(Optional.of(existing));
        when(accountRepository.findByIdAndUserEmail(1L, "user@test.com")).thenReturn(Optional.of(account));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> transactionService.updateTransactionForUser(5L, "user@test.com", buildTransaction(TransactionType.EXPENSE, "30.00"), 1L, 2L));
    }

    @Test
    void deleteForUser_WhenValid_ReversesBalanceAndDeletes() {
        Account account = new Account();
        account.setBalance(new BigDecimal("40.00"));
        Transaction existing = buildTransaction(TransactionType.INCOME, "10.00");
        existing.setAccount(account);

        when(transactionRepository.findByIdAndAccountUserEmail(7L, "user@test.com")).thenReturn(Optional.of(existing));

        transactionService.deleteForUser(7L, "user@test.com");

        assertEquals(new BigDecimal("30.00"), account.getBalance());
        verify(transactionRepository).delete(existing);
    }

    @Test
    void search_ReturnsTransactions() {
        when(transactionRepository.searchUserTransactions("user@test.com", TransactionType.EXPENSE, "Food"))
                .thenReturn(List.of(new Transaction()));

        List<Transaction> result = transactionService.search("user@test.com", TransactionType.EXPENSE, "Food");

        assertEquals(1, result.size());
    }

    private Transaction buildTransaction(TransactionType type, String amount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(type);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDate(LocalDate.of(2026, 5, 1));
        transaction.setDescription("desc");
        return transaction;
    }
}
