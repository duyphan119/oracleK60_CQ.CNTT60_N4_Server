package com.api.shoesshop.controllers;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.shoesshop.dtos.ChangePasswordDTO;
import com.api.shoesshop.dtos.ChangeProfileDTO;
import com.api.shoesshop.dtos.CreateAccountDTO;
import com.api.shoesshop.dtos.LoginDTO;
import com.api.shoesshop.dtos.RefreshTokenDTO;
import com.api.shoesshop.entities.Account;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.services.AccountService;
import com.api.shoesshop.services.AuthService;
import com.api.shoesshop.types.Auth;
import com.api.shoesshop.utils.Helper;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.gson.io.GsonDeserializer;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {
    @Autowired
    private AccountService accountService;

    private AuthService authService = new AuthService();

    @PostMapping("/api/auth/register")
    public ResponseEntity<String> register(@RequestBody CreateAccountDTO body) {
        try {
            Account account = accountService.save(body);
            String accessToken = authService.createAccessToken(account);
            String refreshToken = authService.createRefreshToken(account);
            ResponseCookie.from("refresh_token", refreshToken).httpOnly(true).secure(false)
                    .maxAge(authService.getRefreshTokenExpired());
            return Helper.responseCreated(new Auth(account, accessToken, refreshToken));
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO body, HttpServletResponse res) {
        try {
            Account account = accountService.login(body);
            String accessToken = authService.createAccessToken(account);
            String refreshToken = authService.createRefreshToken(account);
            // ResponseCookie.from("refresh_token",
            // refreshToken).httpOnly(true).secure(false)
            // .maxAge(authService.getRefreshTokenExpired());
            Cookie jwtTokenCookie = new Cookie("refresh_token", refreshToken);
            jwtTokenCookie.setMaxAge(authService.getRefreshTokenExpired());
            jwtTokenCookie.setSecure(true);
            jwtTokenCookie.setHttpOnly(true);
            res.addCookie(jwtTokenCookie);
            return Helper.responseSuccess(new Auth(account, accessToken, refreshToken));
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }
    }

    @GetMapping("/api/auth/my-profile")
    public ResponseEntity<String> getProfile(HttpServletRequest req) {
        System.out.println(
                "------------------------------------------------Get Profile---------------------------------------------------");
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                if (req.getAttribute("account_id") != null) {
                    Optional<Account> optional = accountService
                            .findById(Long.parseLong(req.getAttribute("account_id").toString()));
                    if (optional.isPresent() == true)
                        return Helper.responseSuccess(optional.get());
                }
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }

        return Helper.responseUnauthorized();
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<String> logout() {
        try {
            ResponseCookie.from("refresh_token", null).httpOnly(true).secure(false)
                    .maxAge(0);
            return Helper.responseSuccessNoData();
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }
    }

    @PostMapping("/api/auth/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue(name = "refresh_token") String refreshToken,
            @RequestBody(required = false) RefreshTokenDTO dto, HttpServletResponse res) {
        System.out.println("-----------------refreshToken: " + refreshToken + "-------------");
        try {
            if (refreshToken != null) {
                Claims claims = Jwts.parserBuilder()
                        .deserializeJsonWith(new GsonDeserializer<>(new Gson()))
                        .setSigningKey(new AuthService().getRefreshTokenSecret())
                        .build()
                        .parseClaimsJws(refreshToken)
                        .getBody();
                Account account = new Account();
                account.setId(Long.parseLong(claims.get("id", String.class)));
                account.setAccountRole(claims.get("role", String.class));
                String accessToken = authService.createAccessToken(account);
                String _refreshToken = authService.createRefreshToken(account);
                Cookie jwtTokenCookie = new Cookie("refresh_token", _refreshToken);
                jwtTokenCookie.setMaxAge(authService.getRefreshTokenExpired());
                jwtTokenCookie.setSecure(true);
                jwtTokenCookie.setHttpOnly(true);
                res.addCookie(jwtTokenCookie);
                return Helper.responseSuccess(new Auth(accessToken, _refreshToken));
            } else if (dto != null) {
                Claims claims = Jwts.parserBuilder()
                        .deserializeJsonWith(new GsonDeserializer<>(new Gson()))
                        .setSigningKey(new AuthService().getRefreshTokenSecret())
                        .build()
                        .parseClaimsJws(dto.getRefreshToken())
                        .getBody();
                Account account = new Account();
                account.setId(Long.parseLong(claims.get("id", String.class)));
                account.setAccountRole(claims.get("role", String.class));
                String accessToken = authService.createAccessToken(account);
                String _refreshToken = authService.createRefreshToken(account);
                System.out.println("---------------------------------" + accessToken);
                Cookie jwtTokenCookie = new Cookie("refresh_token", _refreshToken);
                jwtTokenCookie.setMaxAge(authService.getRefreshTokenExpired());
                jwtTokenCookie.setSecure(true);
                jwtTokenCookie.setHttpOnly(true);
                res.addCookie(jwtTokenCookie);
                return Helper.responseSuccess(new Auth(accessToken, _refreshToken));
            }

        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }
        return Helper.responseUnauthorized();
    }

    @PatchMapping("/api/auth/change-profile")
    public ResponseEntity<String> changeProfile(@RequestBody ChangeProfileDTO body, HttpServletRequest req) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                long id = Long.parseLong(req.getAttribute("account_id").toString());
                Account oldAccount = accountService.findById(id).get();
                if (oldAccount != null) {
                    oldAccount.setFullName(body.getFullName());
                    oldAccount.setEmail(body.getEmail());
                    oldAccount.setPhone(body.getPhone());
                    Account newAccount = accountService.update(oldAccount, id);
                    if (newAccount != null) {
                        return Helper.responseSuccess(newAccount);
                    }
                }
                return Helper.responseError();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }

        return Helper.responseUnauthorized();
    }

    @PatchMapping("/api/auth/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO body, HttpServletRequest req) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                long id = Long.parseLong(req.getAttribute("account_id").toString());
                Account oldAccount = accountService.findById(id).get();
                if (oldAccount != null) {
                    if (Helper.verifyPassword(body.getOldPassword(), oldAccount.getHashedPassword()) == true) {
                        Account account = accountService.changePassword(body.getNewPassword(), id);
                        if (account != null) {
                            return Helper.responseSuccessNoData();
                        }
                    }
                }
                return Helper.responseError();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }

        return Helper.responseUnauthorized();
    }
}
