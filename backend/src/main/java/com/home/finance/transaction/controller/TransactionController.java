package com.home.finance.transaction.controller;

import com.home.finance.category.model.TransactionType;
import com.home.finance.transaction.service.TransactionService;
import com.home.finance.transaction.dto.CreateTransactionRequest;
import com.home.finance.transaction.dto.TransactionMapper;
import com.home.finance.transaction.dto.TransactionResponse;
import com.home.finance.transaction.dto.UpdateTransactionRequest;
import com.home.finance.transaction.model.Transaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody CreateTransactionRequest request,
                                                      Authentication authentication) {
        String email = authentication.getName();
        Transaction transaction = transactionMapper.toTransaction(request);
        Transaction createdTransaction = transactionService.createTransactionForUser(
                transaction,
                request.accountId(),
                request.categoryId(),
                email
        );
        TransactionResponse response = transactionMapper.toResponse(createdTransaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Transaction transaction = transactionService.getByIdForUser(id, email);
        TransactionResponse response = transactionMapper.toResponse(transaction);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateTransactionRequest request,
                                                      Authentication authentication) {
        String email = authentication.getName();
        Transaction transaction = transactionMapper.toTransaction(id, request);
        Transaction updatedTransaction = transactionService.updateTransactionForUser(
                id,
                email,
                transaction,
                request.accountId(),
                request.categoryId()
        );
        TransactionResponse response = transactionMapper.toResponse(updatedTransaction);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();

        transactionService.deleteForUser(id, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TransactionResponse>> search(
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) String categoryName,
            Authentication authentication
            ) {

        String email = authentication.getName();

        List<Transaction> transactions = transactionService.search(
                email,
                transactionType,
                categoryName
        );

        List<TransactionResponse> response = transactions.stream()
                .map(transactionMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
