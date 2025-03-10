package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.repository.AccountRepository;
import com.finances.budgetmanagement.repository.UserRepository;
import com.finances.budgetmanagement.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found for user ID: " + userId));
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Account not found for username: " + username));
    }

    public void updateBalanceAfterTransaction(Account account, Transaction transaction, boolean isAdding) {
        BigDecimal amount = transaction.getAmount();

        if (transaction.getTransactionType() == TransactionType.INCOME) {
            account.setBalance(isAdding ? account.getBalance().add(amount) : account.getBalance().subtract(amount));
        } else { // EXPENSE
            account.setBalance(isAdding ? account.getBalance().subtract(amount) : account.getBalance().add(amount));
        }

        updateAccount(account);
    }

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

}
