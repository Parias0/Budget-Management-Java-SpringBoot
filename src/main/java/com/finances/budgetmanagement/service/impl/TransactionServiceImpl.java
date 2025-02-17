package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.entity.Category;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.repository.CategoryRepository;
import com.finances.budgetmanagement.repository.TransactionRepository;
import com.finances.budgetmanagement.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;


    public TransactionServiceImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO){
        Transaction transaction = mapToEntity(transactionDTO);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDTO(savedTransaction);
    }

    @Override
    public List<TransactionDTO>getAllTransactions(){
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO mapToDTO(Transaction transaction){
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setDate(transaction.getDate());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setCategoryName(transaction.getCategory().getName());
        return dto;
    }

    private Transaction mapToEntity(TransactionDTO dto){
        Transaction transaction = new Transaction();
        transaction.setDate(dto.getDate());
        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());

        Category category = categoryRepository.findByName(dto.getCategoryName());
        if (category == null) {
            throw new RuntimeException("Category not found: " + dto.getCategoryName());
        }
        transaction.setCategory(category);
        return transaction;
    }

}
