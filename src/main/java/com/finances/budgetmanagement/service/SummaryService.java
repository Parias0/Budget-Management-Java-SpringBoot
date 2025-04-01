package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.summary.AccountSummaryDTO;
import com.finances.budgetmanagement.dto.summary.CategoryExpenseSummaryDTO;
import com.finances.budgetmanagement.dto.summary.MonthlyCategoryExpensesDTO;

import java.time.YearMonth;
import java.util.List;

public interface SummaryService {

    MonthlyCategoryExpensesDTO getAccountMonthlyCategoryExpenses(Long accountId, YearMonth month);

    List<CategoryExpenseSummaryDTO> getCategoryExpensesForAllAccounts(YearMonth month);

    List<AccountSummaryDTO> getAllAccountsSummary(YearMonth month);
}
