package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.dto.TransactionFilterDTO;
import com.finances.budgetmanagement.entity.Transaction;

import java.time.YearMonth;
import java.util.List;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    TransactionDTO updateTransaction (Long id, TransactionDTO transactionDTO);

    void deleteTransaction (Long id);

    List<TransactionDTO> getAllTransactions();

    List<TransactionDTO> filterTransactions(TransactionFilterDTO filterDTO);
}
