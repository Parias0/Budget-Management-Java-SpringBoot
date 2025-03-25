package com.finances.budgetmanagement.dto;

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
}
