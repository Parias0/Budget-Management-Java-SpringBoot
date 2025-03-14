package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.AccountDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.entity.User;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.repository.AccountRepository;
import com.finances.budgetmanagement.repository.UserRepository;
import com.finances.budgetmanagement.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AccountDTO> getAccountsByUsername(String username) {
        List<Account> accounts = accountRepository.findAllByUser_Username(username);
        if (accounts.isEmpty()) {
            throw new RuntimeException("No accounts found for username: " + username);
        }
        return accounts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public Account getAccountEntityByUsername(String username) {
        return accountRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Account not found for username: " + username));
    }


    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        // Pobieramy aktualnie zalogowanego użytkownika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Account account = new Account();
        account.setName(accountDTO.getName());
        // Jeżeli saldo nie zostało ustawione, ustawiamy domyślnie ZERO
        account.setBalance(accountDTO.getBalance() != null ? accountDTO.getBalance() : BigDecimal.ZERO);
        account.setUser(user);
        Account saved = accountRepository.save(account);
        return convertToDTO(saved);
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        Account account = accountRepository.findById(accountDTO.getId())
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountDTO.getId()));
        account.setName(accountDTO.getName());
        account.setBalance(accountDTO.getBalance());
        Account updated = accountRepository.save(account);
        return convertToDTO(updated);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        accountRepository.delete(account);
    }

    @Override
    public List<AccountDTO> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
    }


    @Override
    public void updateBalanceAfterTransaction(Transaction transaction, boolean isAdding) {
        Account account = transaction.getAccount();
        if (account == null) {
            throw new RuntimeException("Transaction is not associated with any account.");
        }

        BigDecimal amount = transaction.getAmount();
        if (transaction.getTransactionType() == TransactionType.INCOME) {
            account.setBalance(isAdding ? account.getBalance().add(amount) : account.getBalance().subtract(amount));
        } else { // EXPENSE
            account.setBalance(isAdding ? account.getBalance().subtract(amount) : account.getBalance().add(amount));
        }
        accountRepository.save(account);
    }


    // Pomocnicza metoda konwertująca encję na DTO
    private AccountDTO convertToDTO(Account account) {
        return new AccountDTO(account.getId(), account.getBalance(), account.getName());
    }
}
