package com.home.finance.transaction.service;

import com.home.finance.account.model.Account;
import com.home.finance.account.repository.AccountRepository;
import com.home.finance.category.model.Category;
import com.home.finance.category.repository.CategoryRepository;
import com.home.finance.category.model.TransactionType;
import com.home.finance.exception.AccountNotFoundException;
import com.home.finance.exception.CategoryNotFoundException;
import com.home.finance.exception.InvalidTransactionException;
import com.home.finance.exception.InvalidTransactionCategoryException;
import com.home.finance.exception.TransactionNotFoundException;
import com.home.finance.transaction.model.Transaction;
import com.home.finance.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DefaultTransactionService implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public DefaultTransactionService(TransactionRepository transactionRepository,
                                     AccountRepository accountRepository,
                                     CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Transaction> getAllForUser(String email) {
        return transactionRepository.findByAccountUserEmail(email);
    }

    @Override
    public Transaction getByIdForUser(Long id, String email) {
        return transactionRepository.findByIdAndAccountUserEmail(id, email)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found by ID: " + id));
    }

    @Override
    @Transactional
    public Transaction createTransactionForUser(Transaction transaction, Long accountId, Long categoryId, String email) {

        Account account = getAccountForUser(accountId, email);
        Category category = getCategory(categoryId);
        validateCategoryType(transaction, category);

        transaction.setAccount(account);
        transaction.setCategory(category);

        applyToBalance(account, transaction.getTransactionType(), transaction.getAmount());

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransactionForUser(Long id, String email, Transaction transaction, Long accountId, Long categoryId) {

        Transaction existingTransaction = getByIdForUser(id, email);
        Account account = getAccountForUser(accountId, email);
        Category category = getCategory(categoryId);
        validateCategoryType(transaction, category);

        reverseFromBalance(
                existingTransaction.getAccount(),
                existingTransaction.getTransactionType(),
                existingTransaction.getAmount()
        );
        applyToBalance(account, transaction.getTransactionType(), transaction.getAmount());

        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setTransactionType(transaction.getTransactionType());
        existingTransaction.setDate(transaction.getDate());
        existingTransaction.setDescription(transaction.getDescription());
        existingTransaction.setAccount(account);
        existingTransaction.setCategory(category);

        return transactionRepository.save(existingTransaction);
    }

    @Override
    @Transactional
    public void deleteForUser(Long id, String email) {
        Transaction transaction = transactionRepository.findByIdAndAccountUserEmail(id, email)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found by ID: " + id));

        reverseFromBalance(transaction.getAccount(), transaction.getTransactionType(), transaction.getAmount());
        transactionRepository.delete(transaction);
    }

    private Account getAccountForUser(Long accountId, String email) {
        if (accountId == null) {
            throw new InvalidTransactionException("Transaction account is required");
        }

        return accountRepository.findByIdAndUserEmail(accountId, email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found by ID: " + accountId));
    }

    @Override
    public List<Transaction> search(String email, TransactionType transactionType, String categoryName) {
        return transactionRepository.searchUserTransactions(email,  transactionType, categoryName);
    }

    private Category getCategory(Long id) {
        if (id == null) {
            throw new InvalidTransactionException("Transaction category is required");
        }

        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exist by provided ID: " + id));
    }

    private void validateCategoryType(Transaction transaction, Category category) {
        if (!category.getTransactionType().equals(transaction.getTransactionType())) {
            throw new InvalidTransactionCategoryException("Category type does not match transaction type");
        }
    }


    private void applyToBalance(Account account, TransactionType transactionType, BigDecimal amount) {
        BigDecimal currentBalance = getBalance(account);

        if (transactionType == TransactionType.INCOME) {
            account.setBalance(currentBalance.add(amount));
            return;
        }

        account.setBalance(currentBalance.subtract(amount));
    }

    private void reverseFromBalance(Account account, TransactionType transactionType, BigDecimal amount) {
        BigDecimal currentBalance = getBalance(account);

        if (transactionType == TransactionType.INCOME) {
            account.setBalance(currentBalance.subtract(amount));
            return;
        }

        account.setBalance(currentBalance.add(amount));
    }

    private BigDecimal getBalance(Account account) {
        if (account.getBalance() == null) {
            return BigDecimal.ZERO;
        }

        return account.getBalance();
    }
}
