package com.api.shoesshop.services;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.api.shoesshop.entities.Account;
import com.google.gson.Gson;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.gson.io.GsonSerializer;
import io.jsonwebtoken.security.Keys;

public class AuthService {

    private final Key ACCESS_TOKEN_SECRET = Keys
            .hmacShaKeyFor("duy22222222222222221232222222222222222222222222222222222222222222".getBytes());
    private final Key REFRESH_TOKEN_SECRET = Keys
            .hmacShaKeyFor("duy22222122222222221232222222222222222222222222222222222222222222".getBytes());
    private final int REFRESH_TOKEN_EXPIRED = (60 * 24 * 365) * 60000;
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public int getRefreshTokenExpired() {
        return REFRESH_TOKEN_EXPIRED;
    }

    public Key getRefreshTokenSecret() {
        return REFRESH_TOKEN_SECRET;
    }

    public Key getAccessTokenSecret() {
        return ACCESS_TOKEN_SECRET;
    }

    public String getRefreshTokenCookieName() {
        return REFRESH_TOKEN_COOKIE_NAME;
    }

    public String createAccessToken(Account account) {
        Instant now = Instant.now();
        Gson gson = new Gson();
        String access_token = Jwts.builder().serializeToJsonWith(new GsonSerializer<>(gson))
                .claim("id", "" + account.getId())
                .claim("role", "" + account.getAccountRole())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES)))
                .signWith(ACCESS_TOKEN_SECRET, SignatureAlgorithm.HS256)
                .compact();
        return access_token;
    }

    public String createRefreshToken(Account account) {
        Instant now = Instant.now();
        Gson gson = new Gson();
        String refresh_token = Jwts.builder()
                .serializeToJsonWith(new GsonSerializer<>(gson))
                .claim("id", "" + account.getId())
                .claim("role", "" + account.getAccountRole())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(now.plus(REFRESH_TOKEN_EXPIRED / 60000, ChronoUnit.MINUTES)))
                .signWith(REFRESH_TOKEN_SECRET, SignatureAlgorithm.HS256)
                .compact();
        return refresh_token;
    }
}
