package com.finances.budgetmanagement.service.impl;

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
import com.finances.budgetmanagement.utils.SecurityUtil;
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

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        try {
            String username = SecurityUtil.getCurrentUsername();

            Account account;
            // Jeśli DTO zawiera accountId, pobieramy konto po tym ID i weryfikujemy własność
            if (transactionDTO.getAccountId() != null) {
                account = accountService.getAccountById(transactionDTO.getAccountId());
                if (!account.getUser().getUsername().equals(username)) {
                    throw new RuntimeException("Account does not belong to the current user");
                }
            } else {
                // W przeciwnym wypadku pobieramy domyślne konto użytkownika
                account = accountService.getAccountEntityByUsername(username);
            }

            // Mapowanie DTO do encji
            Transaction transaction = mapToEntity(transactionDTO);
            transaction.setAccount(account);

            // Zapisanie transakcji
            Transaction savedTransaction = transactionRepository.save(transaction);

            // Aktualizacja salda po transakcji (dodajemy kwotę)
            accountService.adjustBalance(account, savedTransaction, true);

            TransactionDTO resultDTO = mapToDTO(savedTransaction);
            resultDTO.setAccountId(account.getId());
            return resultDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error creating transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));

        String username = SecurityUtil.getCurrentUsername();


        Account oldAccount = transaction.getAccount();
        if (!oldAccount.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Transaction account does not belong to the current user");
        }

        Account newAccount = oldAccount;
        boolean accountChanged = transactionDTO.getAccountId() != null && !oldAccount.getId().equals(transactionDTO.getAccountId());

        TransactionType oldType = transaction.getTransactionType();
        TransactionType newType = TransactionType.valueOf(transactionDTO.getTransactionType());
        boolean typeChanged = !oldType.equals(newType);

        // Cofnięcie wpływu starej transakcji
        accountService.adjustBalance(oldAccount, transaction, false);

        if (accountChanged) {
            // Pobranie nowego konta
            newAccount = accountService.getAccountById(transactionDTO.getAccountId());
            if (!newAccount.getUser().getUsername().equals(username)) {
                throw new RuntimeException("New account does not belong to the current user");
            }
            transaction.setAccount(newAccount);
        }

        if (typeChanged) {
            transaction.setTransactionType(newType);
        }

        // Aktualizacja danych transakcji
        transaction.setDate(transactionDTO.getDate());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());

        Category category = categoryRepository.findByName(transactionDTO.getCategoryName());
        if (category == null) {
            throw new CategoryNotFoundException("Category not found: " + transactionDTO.getCategoryName());
        }
        transaction.setCategory(category);

        // Zapisanie zmian
        Transaction updatedTransaction = transactionRepository.save(transaction);

        // Zastosowanie wpływu nowej transakcji
        accountService.adjustBalance(newAccount, updatedTransaction, true);

        // Zwrot DTO
        TransactionDTO resultDTO = mapToDTO(updatedTransaction);
        resultDTO.setAccountId(transaction.getAccount().getId());
        return resultDTO;
    }




    @Override
    public void deleteTransaction(Long id) {
        try {
            // Pobieramy transakcję do usunięcia
            Transaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));

            String username = SecurityUtil.getCurrentUsername();

            Account account = transaction.getAccount();

            // Sprawdzenie, czy użytkownik ma dostęp do konta, do którego przypisana jest transakcja
            if (!account.getUser().getUsername().equals(username)) {
                throw new RuntimeException("Transaction does not belong to the current user");
            }

            // Cofamy wpływ transakcji przed jej usunięciem
            accountService.adjustBalance(account,transaction, false);

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
        // Pobieramy transakcje zalogowanego użytkownika
        String username = SecurityUtil.getCurrentUsername();

        return transactionRepository.findByAccountUserUsername(username)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Mapowanie encji Transaction do DTO (dodajemy accountId)
    private TransactionDTO mapToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setDate(transaction.getDate());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setCategoryName(transaction.getCategory().getName());
        dto.setTransactionType(transaction.getTransactionType().toString());
        dto.setAccountId(transaction.getAccount().getId());
        return dto;
    }

    // Mapowanie DTO do encji Transaction
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
