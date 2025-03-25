package com.finances.budgetmanagement.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class MonthlyCategoryExpensesDTO {
    private YearMonth month;
    private BigDecimal totalExpenses;
    private List<CategoryExpensesDTO> categories;


    public MonthlyCategoryExpensesDTO(YearMonth month, BigDecimal totalExpenses, List<CategoryExpensesDTO> categories) {
        this.month = month;
        this.totalExpenses = totalExpenses;
        this.categories = categories;
    }
}
