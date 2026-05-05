package com.home.finance.AccountTest;

import com.home.finance.account.model.Account;
import com.home.finance.account.repository.AccountRepository;
import com.home.finance.account.service.DefaultAccountService;
import com.home.finance.exception.AccountNotFoundException;
import com.home.finance.exception.DuplicateAccountException;
import com.home.finance.transaction.repository.TransactionRepository;
import com.home.finance.user.model.User;
import com.home.finance.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DefaultAccountService accountService;

    @Test
    void getById_WhenAccountExists_ReturnsAccount() {
        Account account = new Account();
        account.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.getById(1L);

        assertEquals(1L, result.getId());
        verify(accountRepository).findById(1L);
    }

    @Test
    void getById_WhenAccountDoesNotExist_ThrowsException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getById(1L));
    }

    @Test
    void createAccountForUser_WhenValid_SavesAccountWithUser() {
        String email = "user@test.com";

        Account account = new Account();
        account.setAccountNumber("LT123");

        User user = new User(email);

        when(accountRepository.existsByAccountNumber("LT123")).thenReturn(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.createAccountForUser(account, email);

        assertEquals(user, result.getUser());
        verify(accountRepository).save(account);
    }

    @Test
    void createAccountForUser_WhenAccountNumberAlreadyExists_ThrowsException() {
        Account account = new Account();
        account.setAccountNumber("LT123");

        when(accountRepository.existsByAccountNumber("LT123")).thenReturn(true);

        assertThrows(DuplicateAccountException.class,
                () -> accountService.createAccountForUser(account, "user@test.com"));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void updateAccountForUser_WhenAccountExists_UpdatesAccountNumber() {
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setAccountNumber("OLD");

        Account updatedData = new Account();
        updatedData.setAccountNumber("NEW");

        when(accountRepository.findByIdAndUserEmail(1L, "user@test.com"))
                .thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        Account result = accountService.updateAccountForUser(1L, "user@test.com", updatedData);

        assertEquals("NEW", result.getAccountNumber());
        verify(accountRepository).save(existingAccount);
    }

    @Test
    void delete_WhenAccountExists_DeletesTransactionsAndAccount() {
        when(accountRepository.existsById(1L)).thenReturn(true);

        accountService.delete(1L);

        verify(transactionRepository).deleteByAccountId(1L);
        verify(accountRepository).deleteById(1L);
    }

    @Test
    void delete_WhenAccountDoesNotExist_ThrowsException() {
        when(accountRepository.existsById(1L)).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> accountService.delete(1L));

        verify(transactionRepository, never()).deleteByAccountId(anyLong());
        verify(accountRepository, never()).deleteById(anyLong());
    }
}