package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.MonthlySummaryDTO;
import com.finances.budgetmanagement.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId);

    TransactionDTO updateTransaction (Long id, TransactionDTO transactionDTO, Long userId);

    void deleteTransaction (Long id, Long userId);

    List<TransactionDTO> getAllTransactions();

    List<MonthlySummaryDTO> getMonthlySummary(int year);

    List<Integer> getAvailableYears();
}
