package com.finances.budgetmanagement.repository;


import com.finances.budgetmanagement.dto.MonthlySummaryDTO;
import com.finances.budgetmanagement.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT NEW com.finances.budgetmanagement.dto.MonthlySummaryDTO(YEAR(t.date), MONTH(t.date), t.category.name, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE YEAR(t.date) = :year " +
            "GROUP BY YEAR(t.date), MONTH(t.date), t.category.name")
    List<MonthlySummaryDTO> getMonthlySummary(@Param("year") int year);

    @Query("SELECT DISTINCT YEAR(t.date) FROM Transaction t ORDER BY YEAR(t.date) DESC")
    List<Integer> findDistinctYears();


}
