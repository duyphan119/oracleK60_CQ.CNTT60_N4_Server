package com.api.shoesshop.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.api.shoesshop.dtos.CreateAccountDTO;
import com.api.shoesshop.dtos.LoginDTO;
import com.api.shoesshop.entities.Account;

public interface AccountService {
    Page<Account> findAll(Map<String, String> query);

    List<Account> search(String q);

    Optional<Account> findById(long id);

    Account save(CreateAccountDTO dto);

    Account update(Account account, long id);

    Account changePassword(String newPassword, long id);

    Account login(LoginDTO dto);

    void delete(long id);

    long count();

}
