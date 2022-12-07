package com.api.shoesshop.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.shoesshop.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    Page<Account> findByAccountRoleNot(String role, Pageable pageable);

    Page<Account> findByEmailContaining(String email, Pageable pageable);

    Page<Account> findByFullNameContaining(String fullName, Pageable pageable);

    Page<Account> findByPhoneContaining(String phone, Pageable pageable);

    Page<Account> findByEmailContainingAndAccountRoleNot(String email, String role, Pageable pageable);

    Page<Account> findByFullNameContainingAndAccountRoleNot(String fullName, String role, Pageable pageable);

    Page<Account> findByPhoneContainingAndAccountRoleNot(String phone, String role, Pageable pageable);
}
