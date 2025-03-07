package com.finances.budgetmanagement.dto;

import com.finances.budgetmanagement.entity.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class MonthlySummaryDTO {

    private int year;
    private int month;
    private String categoryName;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal totalAmount;


    public MonthlySummaryDTO(int year,int month,String categoryName, TransactionType transactionType, BigDecimal totalAmount) {
        this.year = year;
        this.month = month;
        this.categoryName = categoryName;
        this.transactionType = transactionType;
        this.totalAmount = totalAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthName() {
        return Month.of(this.month).getDisplayName(TextStyle.FULL, new Locale("en"));
    }
}