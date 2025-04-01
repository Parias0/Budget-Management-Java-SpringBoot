package com.finances.budgetmanagement.repository;

import com.finances.budgetmanagement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByUserUsername(String username);

    Optional<Account> findByIdAndUserUsername(Long id, String username);

}
