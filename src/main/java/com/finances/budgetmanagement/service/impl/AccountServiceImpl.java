package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.AccountDTO;
import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.User;
import com.finances.budgetmanagement.enums.TransactionType;
import com.finances.budgetmanagement.repository.AccountRepository;
import com.finances.budgetmanagement.repository.UserRepository;
import com.finances.budgetmanagement.service.AccountService;
import com.finances.budgetmanagement.mapper.AccountMapper;
import com.finances.budgetmanagement.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public List<AccountDTO> getAllUserAccounts() {
        String username = SecurityUtil.getCurrentUsername();
        return accountRepository.findAllByUserUsername(username).stream()
                .map(accountMapper::accountToAccountDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Account account = accountMapper.accountDTOToAccount(accountDTO);
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        account.setUser(user);

        Account saved = accountRepository.save(account);
        return accountMapper.accountToAccountDTO(saved);
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        Account account = accountRepository.findById(accountDTO.getId())
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountDTO.getId()));
        account.setName(accountDTO.getName());
        account.setBalance(accountDTO.getBalance());
        Account updated = accountRepository.save(account);
        return accountMapper.accountToAccountDTO(updated);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        accountRepository.delete(account);
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        return accountMapper.accountToAccountDTO(account);
    }

    // Przykładowa metoda adjustBalance operująca na DTO – przyjmuje identyfikator konta i flagę,
    // a przykładowo modyfikuje saldo. W praktyce możesz rozszerzyć tę metodę o przekazywanie danych transakcji.
    @Override
    public AccountDTO adjustBalance(Long accountId, TransactionDTO transactionDTO, boolean isAdding) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        // Jeśli posiadasz TransactionDTO, możesz zmapować go na Transaction lub operować bezpośrednio na danych z DTO.
        BigDecimal amount = transactionDTO.getAmount();
        BigDecimal newBalance = transactionDTO.getTransactionType() == TransactionType.INCOME
                ? account.getBalance().add(isAdding ? amount : amount.negate())
                : account.getBalance().subtract(isAdding ? amount : amount.negate());

        account.setBalance(newBalance);
        Account updated = accountRepository.save(account);
        return accountMapper.accountToAccountDTO(updated);
    }



}
