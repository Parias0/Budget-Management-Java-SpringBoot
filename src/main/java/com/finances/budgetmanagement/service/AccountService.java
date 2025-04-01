package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.account.AccountDTO;
import com.finances.budgetmanagement.dto.transaction.TransactionDTO;

import java.util.List;

public interface AccountService {

    // CRUD
    List<AccountDTO> getAllUserAccounts();
    AccountDTO createAccount(AccountDTO accountDTO);
    AccountDTO updateAccount(AccountDTO accountDTO);
    void deleteAccount(Long accountId);

    //DTO
    AccountDTO getAccountById(Long accountId);


    //business operations
    AccountDTO adjustBalance(Long accountId, TransactionDTO transactionDTO, boolean isAdding);


}
