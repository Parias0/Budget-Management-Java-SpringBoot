package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.AccountDTO;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public ResponseEntity<AccountDTO> getCurrentBalance() {
        // Pobranie zalogowanego użytkownika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Pobierz nazwę użytkownika (np. testuser)

        // Pobranie konta użytkownika na podstawie nazwy użytkownika
        Account account = accountService.getAccountByUsername(username); // Znajdź konto użytkownika

        // Konwersja obiektu Account na AccountDTO
        AccountDTO accountDTO = new AccountDTO(account.getId(), account.getBalance());

        return ResponseEntity.ok(accountDTO); // Zwróć AccountDTO
    }


}
