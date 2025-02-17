package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.TransactionDTO;

import java.util.List;


public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    List<TransactionDTO> getAllTransactions();
}
