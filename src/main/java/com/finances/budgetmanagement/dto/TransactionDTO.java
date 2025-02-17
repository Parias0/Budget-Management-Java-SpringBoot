package com.finances.budgetmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTO {

    private Long id;
    private LocalDate date;
    private BigDecimal amount;
    private String description;
    private String categoryName;


    public TransactionDTO() {
    }

    public TransactionDTO(Long id, String categoryName, String description, BigDecimal amount, LocalDate date) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.amount = amount;
        this.date = date;
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
