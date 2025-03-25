package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.AccountSummaryDTO;
import com.finances.budgetmanagement.dto.CategoryExpenseSummaryDTO;
import com.finances.budgetmanagement.dto.MonthlyCategoryExpensesDTO;

import java.time.YearMonth;
import java.util.List;

public interface SummaryService {

    MonthlyCategoryExpensesDTO getAccountMonthlyCategoryExpenses(Long accountId, YearMonth month);

    List<CategoryExpenseSummaryDTO> getCategoryExpensesForAllAccounts(YearMonth month);

    List<AccountSummaryDTO> getAllAccountsSummary(YearMonth month);
}
