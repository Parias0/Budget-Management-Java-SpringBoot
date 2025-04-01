package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.transaction.TransactionDTO;
import com.finances.budgetmanagement.dto.transaction.TransactionFilterDTO;

import java.util.List;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    TransactionDTO updateTransaction (Long id, TransactionDTO transactionDTO);

    void deleteTransaction (Long id);

    List<TransactionDTO> getAllTransactions();

    List<TransactionDTO> filterTransactions(TransactionFilterDTO filterDTO);
}
