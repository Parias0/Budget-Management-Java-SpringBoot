package com.finances.budgetmanagement.dto.transaction;

import com.finances.budgetmanagement.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTO {

    private Long id;
    private LocalDate date;
    private BigDecimal amount;
    private String description;
    private String categoryName;
    private TransactionType transactionType;
    private Long accountId;


    public TransactionDTO() {
    }

    public TransactionDTO(Long id, LocalDate date, BigDecimal amount, String description, String categoryName, TransactionType transactionType, Long accountId) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.categoryName = categoryName;
        this.transactionType = transactionType;
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
