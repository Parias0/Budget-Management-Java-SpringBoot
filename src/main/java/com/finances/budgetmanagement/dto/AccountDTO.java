package com.finances.budgetmanagement.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccountDTO {

    private Long id;
    private BigDecimal balance;
    private String name;
    private List<TransactionSummaryDTO> transactions;

    public AccountDTO() {
    }

    public AccountDTO(Long id, BigDecimal balance, String name, List<TransactionSummaryDTO> transactions) {
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.transactions = transactions;
    }

    public List<TransactionSummaryDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionSummaryDTO> transactions) {
        this.transactions = transactions;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
