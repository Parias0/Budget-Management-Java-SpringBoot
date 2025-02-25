package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.MonthlySummaryDTO;
import com.finances.budgetmanagement.dto.TransactionDTO;

import java.util.List;


public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    TransactionDTO updateTransaction (Long id, TransactionDTO transactionDTO);

    void deleteTransaction (Long id);

    List<TransactionDTO> getAllTransactions();

    List<MonthlySummaryDTO> getMonthlySummary(int year);

    List<Integer> getAvailableYears();
}
