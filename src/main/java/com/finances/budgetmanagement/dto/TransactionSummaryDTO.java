package com.finances.budgetmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public class TransactionSummaryDTO {
    private Long id;
    private LocalDate date;
    private BigDecimal amount;
    private String description;
    private String categoryName;

    public TransactionSummaryDTO() {
    }

    public TransactionSummaryDTO(Long id, LocalDate date, BigDecimal amount, String description, String categoryName) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
