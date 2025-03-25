package com.finances.budgetmanagement.dto;

import com.finances.budgetmanagement.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionFilterDTO {
    private Long accountId;
    private String category;
    private TransactionType transactionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    public TransactionFilterDTO() {
    }

    public TransactionFilterDTO(Long accountId, String category, TransactionType transactionType, LocalDate startDate, LocalDate endDate, BigDecimal minAmount, BigDecimal maxAmount) {
        this.accountId = accountId;
        this.category = category;
        this.transactionType = transactionType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }
}
