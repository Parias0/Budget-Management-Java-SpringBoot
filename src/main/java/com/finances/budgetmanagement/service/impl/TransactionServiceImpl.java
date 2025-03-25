package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.*;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Category;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.exception.CategoryNotFoundException;
import com.finances.budgetmanagement.exception.TransactionNotFoundException;
import com.finances.budgetmanagement.mapper.TransactionMapper;
import com.finances.budgetmanagement.repository.AccountRepository;
import com.finances.budgetmanagement.repository.CategoryRepository;
import com.finances.budgetmanagement.repository.TransactionRepository;
import com.finances.budgetmanagement.service.AccountService;
import com.finances.budgetmanagement.service.TransactionService;
import com.finances.budgetmanagement.specification.TransactionSpecification;
import com.finances.budgetmanagement.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  CategoryRepository categoryRepository,
                                  AccountRepository accountRepository,
                                  AccountService accountService,
                                  TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        String username = SecurityUtil.getCurrentUsername();
        Account account;

        if (transactionDTO.getAccountId() != null) {
            account = accountRepository.findById(transactionDTO.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found with id: " + transactionDTO.getAccountId()));
            if (!account.getUser().getUsername().equals(username)) {
                throw new RuntimeException("Account does not belong to the current user");
            }
        } else {
            // Pobieramy pierwsze konto użytkownika – można zmodyfikować logikę wyboru domyślnego konta
            account = accountRepository.findAllByUserUsername(username).stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Default account not found for user: " + username));
        }

        Transaction transaction = transactionMapper.transactionDTOToTransaction(transactionDTO);
        transaction.setAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Aktualizacja salda – wykorzystujemy metodę adjustBalance z AccountService, która operuje na DTO
        accountService.adjustBalance(account.getId(), transactionDTO, true);

        TransactionDTO resultDTO = transactionMapper.transactionToTransactionDTO(savedTransaction);
        resultDTO.setAccountId(account.getId());
        return resultDTO;
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

        boolean accountChanged = transactionDTO.getAccountId() != null && !oldAccount.getId().equals(transactionDTO.getAccountId());
        TransactionType oldType = transaction.getTransactionType();
        TransactionType newType = transactionDTO.getTransactionType();
        boolean typeChanged = !oldType.equals(newType);

        // Cofnięcie wpływu starej transakcji
        accountService.adjustBalance(oldAccount.getId(), transactionMapper.transactionToTransactionDTO(transaction), false);

        Account newAccount = oldAccount;
        if (accountChanged) {
            newAccount = accountRepository.findById(transactionDTO.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found with id: " + transactionDTO.getAccountId()));
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

        Transaction updatedTransaction = transactionRepository.save(transaction);

        // Zastosowanie wpływu nowej transakcji
        accountService.adjustBalance(newAccount.getId(), transactionMapper.transactionToTransactionDTO(updatedTransaction), true);

        TransactionDTO resultDTO = transactionMapper.transactionToTransactionDTO(updatedTransaction);
        resultDTO.setAccountId(newAccount.getId());
        return resultDTO;
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));

        String username = SecurityUtil.getCurrentUsername();
        Account account = transaction.getAccount();
        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Transaction does not belong to the current user");
        }

        // Cofamy wpływ transakcji przed usunięciem
        accountService.adjustBalance(account.getId(), transactionMapper.transactionToTransactionDTO(transaction), false);
        transactionRepository.deleteById(id);
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        String username = SecurityUtil.getCurrentUsername();
        return transactionRepository.findByAccountUserUsername(username)
                .stream()
                .map(transactionMapper::transactionToTransactionDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> filterTransactions(TransactionFilterDTO filterDTO) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionSpecification.getSpecification(filterDTO));

        return transactions.stream()
                .map(transactionMapper::transactionToTransactionDTO)
                .collect(Collectors.toList());
    }

}
