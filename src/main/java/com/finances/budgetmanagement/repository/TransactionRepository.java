package com.finances.budgetmanagement.repository;


import com.finances.budgetmanagement.dto.TransactionFilterDTO;
import com.finances.budgetmanagement.dto.TransactionSummary;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findByAccountUserUsername(String username);

    List<Transaction> findByAccountIdAndTransactionTypeAndDateBetween(Long accountId, TransactionType type, LocalDate startDate, LocalDate endDate);


    List<Transaction> findByTransactionTypeAndDateBetween(TransactionType transactionType, LocalDate localDate, LocalDate localDate1);

    @Query("""
        SELECT NEW com.finances.budgetmanagement.dto.TransactionSummary(
            t.transactionType,
            SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.account.id = :accountId
            AND t.date BETWEEN :start AND :end
        GROUP BY t.transactionType
    """)
    List<TransactionSummary> getMonthlySummary(
            @Param("accountId") Long accountId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

}
