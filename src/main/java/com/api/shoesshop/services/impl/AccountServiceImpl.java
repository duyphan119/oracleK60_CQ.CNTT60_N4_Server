package com.api.shoesshop.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.shoesshop.dtos.CreateAccountDTO;
import com.api.shoesshop.dtos.LoginDTO;
import com.api.shoesshop.entities.Account;
import com.api.shoesshop.repositories.AccountRepository;
import com.api.shoesshop.services.AccountService;
import com.api.shoesshop.utils.Helper;
import com.google.gson.Gson;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void delete(long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Page<Account> findAll(Map<String, String> query) {
        String email = query.get("email");
        String fullName = query.get("full_name");
        String phone = query.get("phone");
        Pageable pageable = Helper.getPageable(query);
        if (email != null) {
            return accountRepository.findByEmailContainingAndAccountRoleNot(email, "Admin", pageable);
        } else if (fullName != null) {
            return accountRepository.findByFullNameContainingAndAccountRoleNot(fullName, "Admin", pageable);
        } else if (phone != null) {
            return accountRepository.findByPhoneContainingAndAccountRoleNot(phone, "Admin", pageable);
        }

        return accountRepository.findByAccountRoleNot("Admin", pageable);
    }

    @Override
    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account update(Account variant, long id) {
        Account exiVariant = accountRepository.findById(id).get();
        if (exiVariant != null) {
            exiVariant.setFullName(variant.getFullName());
            exiVariant.setEmail(variant.getEmail());
            exiVariant.setPhone(variant.getPhone());
        }
        return accountRepository.save(exiVariant);
    }

    @Override
    public Account changePassword(String newPassword, long id) {
        Account exiVariant = accountRepository.findById(id).get();
        if (exiVariant != null) {
            exiVariant.setHashedPassword(Helper.hashPassword(newPassword));
        }
        return accountRepository.save(exiVariant);
    }

    @Override
    public List<Account> search(String q) {
        return null;
    }

    @Override
    public long count() {

        return accountRepository.count();
    }

    @Override
    public Account save(CreateAccountDTO dto) {
        Account account = new Account();
        account.setEmail(dto.getEmail());
        account.setHashedPassword(Helper.hashPassword(dto.getPassword()));
        account.setFullName(dto.getFullName());
        account.setPhone(dto.getPhone());
        Account newAccount = accountRepository.save(account);
        return accountRepository.findById(newAccount.getId()).get();
    }

    @Override
    public Account login(LoginDTO dto) {
        Optional<Account> optional = accountRepository.findByEmail(dto.getEmail());
        System.out.println("Calskdwqkj----------------------------" + new Gson().toJson(optional.get()));
        if (optional.isPresent()) {
            Account resAccount = optional.get();
            if (Helper.verifyPassword(dto.getPassword(), resAccount.getHashedPassword()) == true) {
                return resAccount;
            }
        }
        return null;
    }

}
