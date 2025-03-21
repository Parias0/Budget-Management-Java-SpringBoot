package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.AccountDTO;
import com.finances.budgetmanagement.dto.TransactionSummaryDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.entity.User;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.repository.AccountRepository;
import com.finances.budgetmanagement.repository.UserRepository;
import com.finances.budgetmanagement.service.AccountService;
import com.finances.budgetmanagement.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AccountDTO> getAllUserAccounts() {
        String username = SecurityUtil.getCurrentUsername();
        return getAccountsByUsername(username);
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {

        String username = SecurityUtil.getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Account account = new Account();
        account.setName(accountDTO.getName());
        // Jeżeli saldo nie zostało ustawione, ustawiamy domyślnie ZERO
        account.setBalance(accountDTO.getBalance() != null ? accountDTO.getBalance() : BigDecimal.ZERO);
        account.setUser(user);
        Account saved = accountRepository.save(account);
        return convertToDTO(saved);
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        Account account = accountRepository.findById(accountDTO.getId())
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountDTO.getId()));
        account.setName(accountDTO.getName());
        account.setBalance(accountDTO.getBalance());
        Account updated = accountRepository.save(account);
        return convertToDTO(updated);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        accountRepository.delete(account);
    }

    @Override
    public Account getAccountByEntityId(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        return convertToDTO(account);
    }

    @Override
    public List<AccountDTO> getAccountsByUsername(String username) {
        List<Account> accounts = accountRepository.findAllByUser_Username(username);
        if (accounts.isEmpty()) {
            throw new RuntimeException("No accounts found for username: " + username);
        }
        return accounts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public Account getAccountEntityByUsername(String username) {
        return accountRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Account not found for username: " + username));
    }


    @Override
    public void adjustBalance(Account account, Transaction transaction, boolean isAdding) {
        if (account == null) {
            throw new RuntimeException("Transaction is not associated with any account.");
        }

        BigDecimal amount = transaction.getAmount();
        BigDecimal newBalance = transaction.getTransactionType() == TransactionType.INCOME
                ? account.getBalance().add(isAdding ? amount : amount.negate())
                : account.getBalance().subtract(isAdding ? amount : amount.negate());

        account.setBalance(newBalance);
        accountRepository.save(account);
    }


    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setBalance(account.getBalance());

        if(account.getTransactions() != null) {
            dto.setTransactions(account.getTransactions().stream()
                    .map(this::convertTransactionToSummaryDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private TransactionSummaryDTO convertTransactionToSummaryDTO(Transaction transaction) {
        TransactionSummaryDTO dto = new TransactionSummaryDTO();
        dto.setId(transaction.getId());
        dto.setDate(transaction.getDate());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());

        if(transaction.getCategory() != null) {
            dto.setCategoryName(transaction.getCategory().getName());
        }

        return dto;
    }
}
