package com.finances.budgetmanagement.mapper;

import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.entity.Category;
import com.finances.budgetmanagement.entity.Transaction;
import com.finances.budgetmanagement.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {

    @Autowired
    protected CategoryRepository categoryRepository;

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "account.id", target = "accountId")
    public abstract TransactionDTO transactionToTransactionDTO(Transaction transaction);

    @Mapping(source = "categoryName", target = "category")
    @Mapping(target = "account", ignore = true)
    public abstract Transaction transactionDTOToTransaction(TransactionDTO transactionDTO);

    // Metoda pomocnicza wykorzystująca repozytorium
    protected Category map(String categoryName) {
        if (categoryName == null) {
            return null;
        }
        return categoryRepository.findByName(categoryName);
    }
}
