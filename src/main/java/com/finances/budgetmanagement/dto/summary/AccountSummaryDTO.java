package com.finances.budgetmanagement.dto.summary;

import java.math.BigDecimal;

public record AccountSummaryDTO(
        Long accountId,
        String accountName,
        BigDecimal income,
        BigDecimal expense
) {}
