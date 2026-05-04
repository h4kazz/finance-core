package com.home.finance.account.service;

import com.home.finance.account.model.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAll();

    List<Account> getAllForUser(String email);

    Account getByIdForUser(Long id, String email);

    Account getById(Long id);

    Account createAccountForUser(Account account, String email);


    Account updateAccountForUser(Long id, String email, Account account);

    void delete(Long id);
}
