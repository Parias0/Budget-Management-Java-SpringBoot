package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.MonthlyCategoryExpensesResponse;
import com.finances.budgetmanagement.dto.TransactionDTO;

import java.time.YearMonth;
import java.util.List;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    TransactionDTO updateTransaction (Long id, TransactionDTO transactionDTO);

    void deleteTransaction (Long id);

    List<TransactionDTO> getAllTransactions();

    MonthlyCategoryExpensesResponse getAccountMonthlyCategoryExpenses(Long accountId, YearMonth month);
}
