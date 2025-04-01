package com.finances.budgetmanagement.specification;

import com.finances.budgetmanagement.dto.transaction.TransactionFilterDTO;
import com.finances.budgetmanagement.entity.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class TransactionSpecification {

    public static Specification<Transaction> getSpecification(TransactionFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getAccountId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("account").get("id"), filter.getAccountId()));
            }
            if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("name"), filter.getCategory()));
            }
            if (filter.getTransactionType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("transactionType"), filter.getTransactionType()));
            }
            if (filter.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
            }
            if (filter.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
            }
            if (filter.getMinAmount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
            }
            if (filter.getMaxAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
