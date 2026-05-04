package com.home.finance.account.dto;

import com.home.finance.account.model.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMapper {

    public Account toAccount(CreateAccountRequest request) {
        Account account = new Account();

        account.setAccountNumber(request.accountNumber());
        account.setBalance(request.balance());

        return account;
    }

    public Account toAccount(Long id, UpdateAccountRequest request) {
        Account account = new Account();

        account.setId(id);
        account.setAccountNumber(request.accountNumber());

        return account;
    }

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance()
        );
    }

    public List<AccountResponse> toResponseList(List<Account> accounts) {
        List<AccountResponse> responses = new ArrayList<>();

        for (Account account : accounts) {
            responses.add(toResponse(account));
        }

        return responses;
    }
}
