package com.home.finance.account;

import java.util.List;

public interface AccountService {
    List<Account> getAll();

    List<Account> getAllForUser(String email);

    Account getByIdForUser(Long id, String email);

    Account getById(Long id);

    Account createAccountForUser(Account account, String email);

    Account updateAccount(Long id, Account account);

    Account updateAccountForUser(Long id, String email, Account account);

    void delete(Long id);
}
