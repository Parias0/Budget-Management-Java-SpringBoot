package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.CategoryDTO;
import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.service.CategoryService;
import com.finances.budgetmanagement.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:63342")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;


    public TransactionController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<TransactionDTO> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @PostMapping
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transactionDTO){
        return transactionService.createTransaction(transactionDTO);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }


    @PostMapping("/categories")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }
}
