package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.MonthlyReportDTO;
import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.dto.TransactionFilterDTO;
import com.finances.budgetmanagement.enums.TransactionType;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public interface ReportService {

    MonthlyReportDTO generateMonthlyReport(Long accountId, YearMonth month);

    BigDecimal calculateTotal(List<TransactionDTO> transactions, TransactionType type);

    List<TransactionDTO> filterTransactions(TransactionFilterDTO filterDTO);
}
