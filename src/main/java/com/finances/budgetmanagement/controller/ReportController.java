package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.report.MonthlyReportDTO;
import com.finances.budgetmanagement.service.AccountService;
import com.finances.budgetmanagement.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final AccountService accountService;

    public ReportController(ReportService reportService, AccountService accountService) {
        this.reportService = reportService;
        this.accountService = accountService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportDTO> getMonthlyReport(
            @RequestParam Long accountId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        // Walidacja uprawnień do konta
        accountService.getAccountById(accountId);

        MonthlyReportDTO report = reportService.generateMonthlyReport(accountId, month);
        return ResponseEntity.ok(report);
    }
}
