package com.finances.budgetmanagement.service.impl;


import com.finances.budgetmanagement.dto.MonthlyReportDTO;
import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.dto.TransactionFilterDTO;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.mapper.TransactionMapper;
import com.finances.budgetmanagement.repository.TransactionRepository;
import com.finances.budgetmanagement.service.AccountService;
import com.finances.budgetmanagement.service.ReportService;
import com.finances.budgetmanagement.specification.TransactionSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final AccountService accountService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    public ReportServiceImpl(AccountService accountService, TransactionMapper transactionMapper, TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
    }


    public MonthlyReportDTO generateMonthlyReport(Long accountId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        TransactionFilterDTO filter = new TransactionFilterDTO();
        filter.setAccountId(accountId);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);

        List<TransactionDTO> transactions = filterTransactions(filter);

        MonthlyReportDTO report = new MonthlyReportDTO();
        report.setTransactions(transactions);

        // Oblicz sumy
        report.setTotalIncome(calculateTotal(transactions, TransactionType.INCOME));
        report.setTotalExpense(calculateTotal(transactions, TransactionType.EXPENSE));

        // Grupuj po kategoriach
        report.setCategoryBreakdown(transactions.stream()
                .collect(Collectors.groupingBy(
                        TransactionDTO::getCategoryName,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                TransactionDTO::getAmount,
                                BigDecimal::add
                        )
                )));

        return report;
    }

    public BigDecimal calculateTotal(List<TransactionDTO> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == type)
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public List<TransactionDTO> filterTransactions(TransactionFilterDTO filterDTO) {
        // 1. Walidacja uprawnień do konta
        if(filterDTO.getAccountId() != null) {
            accountService.getAccountById(filterDTO.getAccountId());
        }

        // 2. Budowanie specyfikacji
        Specification<Transaction> spec = TransactionSpecification.getSpecification(filterDTO);

        // 3. Pobieranie transakcji z repozytorium
        List<Transaction> transactions = transactionRepository.findAll(spec);

        // 4. Mapowanie na DTO
        return transactions.stream()
                .map(transactionMapper::transactionToTransactionDTO)
                .collect(Collectors.toList());
    }
}
