package com.finances.budgetmanagement.dto;

import java.math.BigDecimal;
import java.util.List;

public class CategoryExpensesDTO {
    private String categoryName;
    private BigDecimal totalAmount;
    private List<TransactionDTO> transactions;

    public CategoryExpensesDTO() {
    }

    public CategoryExpensesDTO(String categoryName, BigDecimal totalAmount, List<TransactionDTO> transactions) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
        this.transactions = transactions;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
