package com.api.shoesshop.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.shoesshop.dtos.CreateAccountDTO;
import com.api.shoesshop.entities.Account;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.services.AccountService;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AccountController {

        @Autowired
        private AccountService accountService;

        @GetMapping("/api/account")
        public ResponseEntity<String> findAllAccounts(HttpServletRequest req, @RequestParam Map<String, String> query) {
                if (AuthInterceptor.isAdmin(req) == true) {
                        try {
                                Page<Account> page = accountService.findAll(query);
                                return Helper.responseSuccess(
                                                new FindAll<>(page.getContent(), page.getTotalElements()));
                        } catch (Exception e) {
                                System.out.println(e);
                                return Helper.responseError();
                        }
                }
                return Helper.responseUnauthorized();
        }

        @PostMapping("/api/account")
        public ResponseEntity<String> createAccount(HttpServletRequest req, @RequestBody CreateAccountDTO body) {
                if (AuthInterceptor.isAdmin(req) == true) {
                        try {
                                Account account = accountService.save(body);
                                account.setHashedPassword(null);
                                return Helper.responseCreated(account);
                        } catch (Exception e) {
                                System.out.println(e);
                                return Helper.responseError();
                        }
                }
                return Helper.responseUnauthorized();
        }

        @GetMapping("/api/account/{id}")
        public ResponseEntity<String> findById(HttpServletRequest req, @PathVariable(name = "id") long id) {
                if (AuthInterceptor.isAdmin(req) == true) {
                        try {
                                Account account = accountService.findById(id).get();
                                if (account == null) {
                                        return Helper.responseError();
                                }
                                return Helper.responseSuccess(account);
                        } catch (Exception e) {
                                System.out.println(e);
                                return Helper.responseError();
                        }
                }
                return Helper.responseUnauthorized();
        }

        @PatchMapping("/api/account/{id}")
        public ResponseEntity<String> update(HttpServletRequest req, @RequestBody Account body,
                        @PathVariable(name = "id") long id) {
                if (AuthInterceptor.isAdmin(req) == true) {
                        try {
                                Account account = accountService.update(body, id);
                                account.setHashedPassword(null);
                                return Helper.responseSuccess(account);
                        } catch (Exception e) {
                                System.out.println(e);
                                return Helper.responseError();
                        }
                }
                return Helper.responseUnauthorized();
        }

        @DeleteMapping("/api/account/{id}")
        public ResponseEntity<String> delete(HttpServletRequest req, @PathVariable(name = "id") long id) {
                if (AuthInterceptor.isAdmin(req) == true) {
                        try {
                                accountService.delete(id);
                                return Helper.responseSuccessNoData();
                        } catch (Exception e) {
                                System.out.println(e);
                                return Helper.responseError();
                        }
                }
                return Helper.responseUnauthorized();
        }
}
