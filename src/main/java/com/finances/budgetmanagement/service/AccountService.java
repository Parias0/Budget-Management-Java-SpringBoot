package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.AccountDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Transaction;
import java.util.List;

public interface AccountService {

    List<AccountDTO> getAllUserAccounts();

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO updateAccount(AccountDTO accountDTO);

    void deleteAccount(Long accountId);

    Account getAccountByEntityId(Long accountId);

    AccountDTO getAccountById(Long accountId);

    List<AccountDTO> getAccountsByUsername(String username);

    Account getAccountEntityByUsername(String username);

    void adjustBalance(Account account, Transaction transaction, boolean isAdding);

}
