package com.home.finance.account;

import com.home.finance.account.dto.AccountResponse;
import com.home.finance.account.dto.CreateAccountRequest;
import com.home.finance.account.dto.UpdateAccountRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request,
                                                  Authentication authentication) {
        String email = authentication.getName();
        Account account = accountMapper.toAccount(request);
        Account createdAccount = accountService.createAccountForUser(account, email);
        AccountResponse response = accountMapper.toResponse(createdAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<AccountResponse> getAll(Authentication authentication) {
        String email = authentication.getName();
        List<Account> accounts = accountService.getAllForUser(email);
        return accountMapper.toResponseList(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Account account = accountService.getByIdForUser(id, email);

        AccountResponse response = accountMapper.toResponse(account);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(@PathVariable Long id,
                                                  @RequestBody UpdateAccountRequest request,
                                                  Authentication authentication) {
        String email = authentication.getName();
        Account account = accountMapper.toAccount(id, request);
        Account updatedAccount = accountService.updateAccountForUser(id, email, account);
        AccountResponse response = accountMapper.toResponse(updatedAccount);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();

        Account account = accountService.getByIdForUser(id, email);
        accountService.delete(account.getId());
        return ResponseEntity.noContent().build();
    }
}
