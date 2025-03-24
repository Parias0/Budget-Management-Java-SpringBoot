package com.finances.budgetmanagement.dto;

import java.math.BigDecimal;

public record AccountSummaryDTO(
        Long accountId,
        String accountName,
        BigDecimal income,
        BigDecimal expense
) {}
