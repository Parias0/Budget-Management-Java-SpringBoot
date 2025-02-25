package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public ResponseEntity<Account> getCurrentBalance(){
        Account account = accountService.getAccountById(1L);
        return ResponseEntity.ok(account);
    }
}
