package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.MonthlyCategoryExpensesResponse;
import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping
    public List<TransactionDTO> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO){
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted");
    }

    @GetMapping("/account-category-expenses")
    public ResponseEntity<MonthlyCategoryExpensesResponse> getAccountMonthlyCategoryExpenses(
            @RequestParam Long accountId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        MonthlyCategoryExpensesResponse response = transactionService.getAccountMonthlyCategoryExpenses(accountId, month);
        return ResponseEntity.ok(response);
    }


}
