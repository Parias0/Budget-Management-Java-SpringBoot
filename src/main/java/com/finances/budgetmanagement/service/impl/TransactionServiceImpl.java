package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.MonthlySummaryDTO;
import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Category;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.exception.CategoryNotFoundException;
import com.finances.budgetmanagement.exception.TransactionNotFoundException;
import com.finances.budgetmanagement.repository.CategoryRepository;
import com.finances.budgetmanagement.repository.TransactionRepository;
import com.finances.budgetmanagement.service.AccountService;
import com.finances.budgetmanagement.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountService accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.accountService = accountService;
    }

    public TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId) {
        try {
            Account account = accountService.getAccountByUserId(userId);

            // Mapowanie DTO do encji
            Transaction transaction = mapToEntity(transactionDTO);
            transaction.setAccount(account); // przypisujemy konto użytkownika do transakcji

            // Zapisanie transakcji
            Transaction savedTransaction = transactionRepository.save(transaction);

            // Aktualizacja salda po transakcji
            accountService.updateBalanceAfterTransaction(account, savedTransaction, true);

            return mapToDTO(savedTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Error creating transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO, Long userId) {
        try {
            // Pobieramy transakcję
            Transaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));

            // Pobieramy konto użytkownika
            Account account = accountService.getAccountByUserId(userId);

            // Cofamy poprzednią transakcję
            accountService.updateBalanceAfterTransaction(account, transaction, false);

            // Mapowanie danych do zaktualizowanej transakcji
            transaction.setDate(transactionDTO.getDate());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDescription(transactionDTO.getDescription());
            Category category = categoryRepository.findByName(transactionDTO.getCategoryName());
            if (category == null) {
                throw new CategoryNotFoundException("Category not found: " + transactionDTO.getCategoryName());
            }
            transaction.setCategory(category);
            transaction.setTransactionType(TransactionType.valueOf(transactionDTO.getTransactionType()));

            // Zapisanie transakcji
            Transaction updatedTransaction = transactionRepository.save(transaction);

            // Zastosowanie nowej transakcji
            accountService.updateBalanceAfterTransaction(account, updatedTransaction, true);

            return mapToDTO(updatedTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Error updating transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteTransaction(Long id, Long userId) {
        try {
            // Pobieramy transakcję
            Transaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));

            // Pobieramy konto użytkownika
            Account account = accountService.getAccountByUserId(userId);

            // Cofamy wpływ transakcji przed jej usunięciem
            accountService.updateBalanceAfterTransaction(account, transaction, false);

            // Usuwamy transakcję
            transactionRepository.deleteById(id);
        } catch (TransactionNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlySummaryDTO> getMonthlySummary(int year) {
        return transactionRepository.getMonthlySummary(year);
    }

    @Override
    public List<Integer> getAvailableYears() {
        return transactionRepository.findDistinctYears();
    }

    private TransactionDTO mapToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setDate(transaction.getDate());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setCategoryName(transaction.getCategory().getName());
        dto.setTransactionType(transaction.getTransactionType().toString());
        return dto;
    }

    private Transaction mapToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setDate(dto.getDate());
        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());

        Category category = categoryRepository.findByName(dto.getCategoryName());
        if (category == null) {
            throw new RuntimeException("Category not found: " + dto.getCategoryName());
        }
        transaction.setCategory(category);
        transaction.setTransactionType(TransactionType.valueOf(dto.getTransactionType()));
        return transaction;
    }

}
