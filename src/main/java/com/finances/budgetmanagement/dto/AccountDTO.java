package com.finances.budgetmanagement.dto;

import java.math.BigDecimal;

public class AccountDTO {

    private Long id;
    private BigDecimal balance;
    private String name;

    public AccountDTO(Long id, BigDecimal balance, String name) {
        this.id = id;
        this.balance = balance;
        this.name = name;
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
