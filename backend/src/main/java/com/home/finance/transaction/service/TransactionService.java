package com.home.finance.transaction.service;

import com.home.finance.category.model.TransactionType;
import com.home.finance.transaction.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAllForUser(String email);

    Transaction getByIdForUser(Long id, String email);

    Transaction createTransactionForUser(Transaction transaction, Long accountId, Long categoryId, String email);

    Transaction updateTransactionForUser(Long id, String email, Transaction transaction, Long accountId, Long categoryId);

    void deleteForUser(Long id, String email);

    List<Transaction> search(
            String email,
            TransactionType transactionType,
            String categoryName
    );
}
