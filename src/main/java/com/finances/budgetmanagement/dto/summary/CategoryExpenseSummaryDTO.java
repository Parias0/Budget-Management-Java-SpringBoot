package com.finances.budgetmanagement.dto.summary;

import java.math.BigDecimal;

public class CategoryExpenseSummaryDTO {

    private String categoryName;
    private String accountName;
    private BigDecimal totalAmount;


    public CategoryExpenseSummaryDTO(String categoryName, String accountName, BigDecimal totalAmount) {
        this.categoryName = categoryName;
        this.accountName = accountName;
        this.totalAmount = totalAmount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
}
