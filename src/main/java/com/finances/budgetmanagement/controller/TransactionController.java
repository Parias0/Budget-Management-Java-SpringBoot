package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.transaction.TransactionDTO;
import com.finances.budgetmanagement.dto.transaction.TransactionFilterDTO;
import com.finances.budgetmanagement.entity.Category;
import com.finances.budgetmanagement.exception.CategoryNotFoundException;
import com.finances.budgetmanagement.repository.CategoryRepository;
import com.finances.budgetmanagement.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryRepository categoryRepository;


    public TransactionController(TransactionService transactionService, CategoryRepository categoryRepository) {
        this.transactionService = transactionService;
        this.categoryRepository = categoryRepository;
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

    @GetMapping("/filter")
    public List<TransactionDTO> filterTransactions(
            @RequestParam(required = false) Long accountId,
            @RequestParam(name = "category", required = false) String categoryName,  // Poprawione mapowanie nazwy parametru
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount
    ) {
        TransactionFilterDTO filterDTO = new TransactionFilterDTO();
        filterDTO.setAccountId(accountId);

        if (categoryName != null) {
            Category foundCategory = categoryRepository.findByName(categoryName);
            if (foundCategory == null) {
                throw new CategoryNotFoundException("Category not found: " + categoryName);
            }
            filterDTO.setCategory(foundCategory.getName());
        }


        if (transactionType != null && !transactionType.isEmpty()) {
            try {
                filterDTO.setTransactionType(Enum.valueOf(
                        com.finances.budgetmanagement.enums.TransactionType.class,
                        transactionType.toUpperCase()
                ));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
            }
        }

        filterDTO.setStartDate(startDate);
        filterDTO.setEndDate(endDate);
        filterDTO.setMinAmount(minAmount);
        filterDTO.setMaxAmount(maxAmount);

        return transactionService.filterTransactions(filterDTO);
    }


}
