package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.AccountSummaryDTO;
import com.finances.budgetmanagement.dto.CategoryExpenseSummaryDTO;
import com.finances.budgetmanagement.dto.MonthlyCategoryExpensesDTO;
import com.finances.budgetmanagement.service.SummaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }


    @GetMapping("/account-summary")
    public ResponseEntity<List<AccountSummaryDTO>> getAllAccountsSummary(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        return ResponseEntity.ok(summaryService.getAllAccountsSummary(month));
    }

    @GetMapping("/account-category-expenses")
    public ResponseEntity<MonthlyCategoryExpensesDTO> getAccountMonthlyCategoryExpenses(
            @RequestParam Long accountId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        MonthlyCategoryExpensesDTO response = summaryService.getAccountMonthlyCategoryExpenses(accountId, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category-expenses-summary")
    public ResponseEntity<List<CategoryExpenseSummaryDTO>> getCategoryExpensesSummary(@RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        List<CategoryExpenseSummaryDTO> response = summaryService.getCategoryExpensesForAllAccounts(month);
        return ResponseEntity.ok(response);
    }
}
