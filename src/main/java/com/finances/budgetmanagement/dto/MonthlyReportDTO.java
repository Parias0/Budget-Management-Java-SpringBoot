package com.finances.budgetmanagement.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MonthlyReportDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private Map<String, BigDecimal> categoryBreakdown;
    private List<TransactionDTO> transactions;


    public MonthlyReportDTO() {
    }


    public MonthlyReportDTO(BigDecimal totalIncome, BigDecimal totalExpense, Map<String, BigDecimal> categoryBreakdown, List<TransactionDTO> transactions) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.categoryBreakdown = categoryBreakdown;
        this.transactions = transactions;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Map<String, BigDecimal> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, BigDecimal> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
