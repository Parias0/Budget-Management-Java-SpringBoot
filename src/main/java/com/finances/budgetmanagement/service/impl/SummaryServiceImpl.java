package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.summary.AccountSummaryDTO;
import com.finances.budgetmanagement.dto.summary.CategoryExpenseSummaryDTO;
import com.finances.budgetmanagement.dto.summary.CategoryExpensesDTO;
import com.finances.budgetmanagement.dto.summary.MonthlyCategoryExpensesDTO;
import com.finances.budgetmanagement.dto.transaction.TransactionDTO;
import com.finances.budgetmanagement.dto.transaction.TransactionFilterDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.mapper.TransactionMapper;
import com.finances.budgetmanagement.repository.AccountRepository;
import com.finances.budgetmanagement.repository.TransactionRepository;
import com.finances.budgetmanagement.service.SummaryService;
import com.finances.budgetmanagement.specification.TransactionSpecification;
import com.finances.budgetmanagement.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public SummaryServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public MonthlyCategoryExpensesDTO getAccountMonthlyCategoryExpenses(Long accountId, YearMonth month) {

        String username = SecurityUtil.getCurrentUsername();

        Account account = accountRepository.findByIdAndUserUsername(accountId, username)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        TransactionFilterDTO filter = new TransactionFilterDTO();
        filter.setAccountId(accountId);
        filter.setTransactionType(TransactionType.EXPENSE);
        filter.setStartDate(month.atDay(1));
        filter.setEndDate(month.atEndOfMonth());

        List<Transaction> expenses = transactionRepository.findAll(
                TransactionSpecification.getSpecification(filter)
        );

        Map<String, List<Transaction>> transactionsByCategory = expenses.stream()
                .collect(Collectors.groupingBy(t -> t.getCategory().getName()));

        List<CategoryExpensesDTO> categoryExpenses = transactionsByCategory.entrySet().stream()
                .map(entry -> {
                    BigDecimal total = entry.getValue().stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    List<TransactionDTO> transactions = entry.getValue().stream()
                            .map(transactionMapper::transactionToTransactionDTO)
                            .toList();
                    return new CategoryExpensesDTO(entry.getKey(), total, transactions);
                })
                .toList();

        BigDecimal totalExpenses = categoryExpenses.stream()
                .map(CategoryExpensesDTO::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlyCategoryExpensesDTO(month, totalExpenses, categoryExpenses);
    }

    @Override
    public List<CategoryExpenseSummaryDTO> getCategoryExpensesForAllAccounts(YearMonth month) {
        TransactionFilterDTO filter = new TransactionFilterDTO();
        filter.setTransactionType(TransactionType.EXPENSE);
        filter.setStartDate(month.atDay(1));
        filter.setEndDate(month.atEndOfMonth());

        List<Transaction> expenses = transactionRepository.findAll(
                TransactionSpecification.getSpecification(filter)
        );

        Map<String, BigDecimal> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        return categoryTotals.entrySet().stream()
                .map(entry -> new CategoryExpenseSummaryDTO(entry.getKey(), null, entry.getValue()))
                .toList();
    }

    @Override
    public List<AccountSummaryDTO> getAllAccountsSummary(YearMonth month) {
        String username = SecurityUtil.getCurrentUsername();

        // Pobieramy tylko konta bieżącego użytkownika!
        List<Account> userAccounts = accountRepository.findAllByUserUsername(username);

        return userAccounts.stream() // Zmiana z accountRepository.findAll()
                .map(account -> {
                    TransactionFilterDTO filter = new TransactionFilterDTO();
                    filter.setAccountId(account.getId());
                    filter.setStartDate(month.atDay(1));
                    filter.setEndDate(month.atEndOfMonth());

                    Map<TransactionType, BigDecimal> summary = transactionRepository.findAll(
                                    TransactionSpecification.getSpecification(filter)
                            ).stream()
                            .collect(Collectors.groupingBy(
                                    Transaction::getTransactionType,
                                    Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                            ));

                    return new AccountSummaryDTO(
                            account.getId(),
                            account.getName(),
                            summary.getOrDefault(TransactionType.INCOME, BigDecimal.ZERO),
                            summary.getOrDefault(TransactionType.EXPENSE, BigDecimal.ZERO)
                    );
                })
                .toList();
    }
}