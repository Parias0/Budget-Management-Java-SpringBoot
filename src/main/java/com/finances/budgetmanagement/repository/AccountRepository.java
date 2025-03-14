package com.finances.budgetmanagement.repository;

import com.finances.budgetmanagement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUser_Username(String username);

    List<Account> findAllByUserId(Long userId);

    List<Account> findAllByUser_Username(String username);
}
