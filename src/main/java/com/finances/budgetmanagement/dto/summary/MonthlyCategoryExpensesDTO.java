package com.finances.budgetmanagement.dto.summary;

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

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public List<CategoryExpensesDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryExpensesDTO> categories) {
        this.categories = categories;
    }
}
