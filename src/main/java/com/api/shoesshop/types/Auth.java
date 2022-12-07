package com.api.shoesshop.types;

import com.api.shoesshop.entities.Account;

public class Auth {
    private Account account;
    private String accessToken, refreshToken;

    public Auth() {

    }

    public Auth(Account account, String accessToken, String refreshToken) {
        this.account = account;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Auth(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
