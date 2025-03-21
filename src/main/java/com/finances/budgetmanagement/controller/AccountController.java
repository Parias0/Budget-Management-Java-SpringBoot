package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.AccountDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    public ResponseEntity<List<AccountDTO>> getUserAccounts() {
        List<AccountDTO> accounts = accountService.getAllUserAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }


    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }


    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long accountId, @RequestBody AccountDTO accountDTO) {
        accountDTO.setId(accountId);
        AccountDTO updated = accountService.updateAccount(accountDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted");
    }
}
