package com.home.finance.category.repository;

import com.home.finance.category.model.Category;
import com.home.finance.category.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndTransactionType(String name, TransactionType transactionType);

    boolean existsByNameAndTransactionTypeAndIdNot(String name, TransactionType transactionType, Long id);
}
