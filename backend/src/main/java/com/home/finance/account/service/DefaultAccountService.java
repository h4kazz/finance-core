package com.home.finance.account.service;

import com.home.finance.account.model.Account;
import com.home.finance.account.repository.AccountRepository;
import com.home.finance.exception.AccountNotFoundException;
import com.home.finance.exception.DuplicateAccountException;
import com.home.finance.user.model.User;
import com.home.finance.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAccountService implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public DefaultAccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getAllForUser(String email) {
        return accountRepository.findByUserEmail(email);
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public Account getByIdForUser(Long id, String email) {
        return accountRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public Account createAccountForUser(Account account, String email) {
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new DuplicateAccountException(account.getAccountNumber());
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        account.setUser(user);
        return accountRepository.save(account);
    }


    @Override
    public Account updateAccountForUser(Long id, String email, Account account) {
        Account existingAccount = accountRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (existingAccount.getAccountNumber().equals(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account number is already " + account.getAccountNumber());
        }

        existingAccount.setAccountNumber(account.getAccountNumber());
        return accountRepository.save(existingAccount);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
