package com.home.finance.account;

import com.home.finance.account.dto.AccountResponse;
import com.home.finance.account.dto.CreateAccountRequest;
import com.home.finance.exception.AccountNotFoundException;
import com.home.finance.exception.DuplicateAccountException;
import com.home.finance.user.User;
import com.home.finance.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
                .orElseThrow(() -> new AccountNotFoundException("Account does not exist by provided id: " + id));
    }

    @Override
    public Account getByIdForUser(Long id, String email) {
        return accountRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found by ID: " + id));
    }

    @Override
    public Account createAccountForUser(Account account, String email) {
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new DuplicateAccountException("Account already exist provided account number: " + account.getAccountNumber());
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        account.setUser(user);
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long id, Account account) {
        Account existingAccount = getById(id);

        if (existingAccount.getAccountNumber().equals(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account number is already " + account.getAccountNumber());
        }

        existingAccount.setAccountNumber(account.getAccountNumber());

        return accountRepository.save(existingAccount);
    }

    @Override
    public Account updateAccountForUser(Long id, String email, Account account) {
        Account existingAccount = accountRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));

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
