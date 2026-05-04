package com.home.finance.transaction.dto;

import com.home.finance.transaction.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    public Transaction toTransaction(CreateTransactionRequest request) {
        Transaction transaction = new Transaction();

        transaction.setAmount(request.amount());
        transaction.setTransactionType(request.transactionType());
        transaction.setDate(request.date());
        transaction.setDescription(request.description());

        return transaction;
    }

    public Transaction toTransaction(Long id, UpdateTransactionRequest request) {
        Transaction transaction = new Transaction();

        transaction.setId(id);
        transaction.setAmount(request.amount());
        transaction.setTransactionType(request.transactionType());
        transaction.setDate(request.date());
        transaction.setDescription(request.description());

        return transaction;
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAccount().getId(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    public List<TransactionResponse> toResponseList(List<Transaction> transactions) {
        List<TransactionResponse> responses = new ArrayList<>();

        for (Transaction transaction : transactions) {
            responses.add(toResponse(transaction));
        }

        return responses;
    }
}
