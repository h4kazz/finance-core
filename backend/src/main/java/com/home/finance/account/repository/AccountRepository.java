package com.home.finance.account.repository;

import com.home.finance.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String number);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByUserEmail(String email);

    Optional<Account> findByIdAndUserEmail(Long id, String email);

}
