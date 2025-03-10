package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Transaction;

public interface AccountService {

    Account getAccountByUserId (Long userId);

    Account getAccountByUsername(String username);

    Account updateAccount (Account account);

    void updateBalanceAfterTransaction(Account account, Transaction transaction, boolean isAdding);
}
