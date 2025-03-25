package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.*;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Category;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.mapper.TransactionMapper;
import com.finances.budgetmanagement.repository.AccountRepository;
import com.finances.budgetmanagement.repository.TransactionRepository;
import com.finances.budgetmanagement.service.SummaryService;
import com.finances.budgetmanagement.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        String username = SecurityUtil.getCurrentUsername();

        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Account does not belong to the current user");
        }

        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        List<Transaction> expenses = transactionRepository.findByAccountIdAndTransactionTypeAndDateBetween(
                accountId,
                TransactionType.EXPENSE,
                startDate,
                endDate
        );

        // Grupowanie transakcji po kategoriach
        Map<Category, List<Transaction>> transactionsByCategory = expenses.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory));

        // Tworzenie DTO dla kategorii
        List<CategoryExpensesDTO> categoryExpenses = transactionsByCategory.entrySet().stream()
                .map(entry -> {
                    Category category = entry.getKey();
                    List<Transaction> categoryTransactions = entry.getValue();
                    BigDecimal categoryTotal = categoryTransactions.stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    List<TransactionDTO> transactionsDTO = categoryTransactions.stream()
                            .map(transactionMapper::transactionToTransactionDTO)
                            .collect(Collectors.toList());
                    return new CategoryExpensesDTO(category.getName(), categoryTotal, transactionsDTO);
                })
                .collect(Collectors.toList());

        BigDecimal total = categoryExpenses.stream()
                .map(CategoryExpensesDTO::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlyCategoryExpensesDTO(month, total, categoryExpenses);
    }

    public List<CategoryExpenseSummaryDTO> getCategoryExpensesForAllAccounts(YearMonth month) {
        // Pobieramy transakcje wydatków z bazy dla wszystkich kont w danym miesiącu
        List<Transaction> expenses = transactionRepository.findByTransactionTypeAndDateBetween(
                TransactionType.EXPENSE, month.atDay(1), month.atEndOfMonth()
        );

        // Grupowanie po kategoriach, sumowanie wydatków
        Map<String, BigDecimal> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        // Przekształcamy dane na DTO
        return categoryTotals.entrySet().stream()
                .map(entry -> new CategoryExpenseSummaryDTO(entry.getKey(), null, entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<AccountSummaryDTO> getAllAccountsSummary(YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        return accountRepository.findAll().stream()
                .map(account -> {
                    Map<TransactionType, BigDecimal> summary = transactionRepository
                            .getMonthlySummary(account.getId(), startDate, endDate)
                            .stream()
                            .collect(Collectors.toMap(
                                    TransactionSummary::transactionType,
                                    TransactionSummary::total
                            ));

                    return new AccountSummaryDTO(
                            account.getId(),
                            account.getName(),
                            summary.getOrDefault(TransactionType.INCOME, BigDecimal.ZERO),
                            summary.getOrDefault(TransactionType.EXPENSE, BigDecimal.ZERO)
                    );
                })
                .collect(Collectors.toList());
    }
}
