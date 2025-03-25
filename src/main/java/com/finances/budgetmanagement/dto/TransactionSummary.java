package com.finances.budgetmanagement.dto;

import com.finances.budgetmanagement.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionSummary(
        TransactionType transactionType,
        BigDecimal total
) {

}
