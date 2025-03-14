package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.AccountDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Transaction;
import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccountsByUsername(String username);

    Account getAccountEntityByUsername(String username);

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO updateAccount(AccountDTO accountDTO);

    void deleteAccount(Long accountId);

    List<AccountDTO> getAccountsByUserId(Long userId);

    Account getAccountById(Long accountId);


    // Pozostawiamy metodę aktualizacji salda – operuje na encji, gdyż transakcje mogą być zewnętrzną logiką
    void updateBalanceAfterTransaction(Transaction transaction, boolean isAdding);
}
