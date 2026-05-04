package com.home.finance.transaction.repository;

import com.home.finance.category.model.TransactionType;
import com.home.finance.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountUserEmail(String email);

    Optional<Transaction> findByIdAndAccountUserEmail(Long id, String email);

    boolean existsByCategoryId(Long categoryId);

    @Query("""
    SELECT t
    FROM Transaction t
    WHERE t.account.user.email = :email
      AND (:transactionType IS NULL OR t.transactionType = :transactionType)
      AND (:categoryName IS NULL OR LOWER(t.category.name) LIKE LOWER(CONCAT('%', :categoryName, '%')))
""")
    List<Transaction> searchUserTransactions(
            @Param("email") String email,
            @Param("transactionType") TransactionType transactionType,
            @Param("categoryName") String categoryName
    );
}
