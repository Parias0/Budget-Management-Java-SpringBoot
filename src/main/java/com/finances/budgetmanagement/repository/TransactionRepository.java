package com.finances.budgetmanagement.repository;


import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountUserUsername(String username);

    List<Transaction> findByAccountIdAndTransactionTypeAndDateBetween(Long accountId, TransactionType type, LocalDate startDate, LocalDate endDate);



}
