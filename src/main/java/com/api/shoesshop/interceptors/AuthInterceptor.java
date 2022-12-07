package com.api.shoesshop.interceptors;

import javax.servlet.http.HttpServletRequest;

import com.api.shoesshop.services.AuthService;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.gson.io.GsonDeserializer;

public class AuthInterceptor {
    private static Gson gson = new Gson();

    public static boolean isLoggedin(HttpServletRequest req) {
        String reqHeader = req.getHeader("authorization");
        if (reqHeader != null && reqHeader.startsWith("Bearer ") == true) {
            String access_token = reqHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .deserializeJsonWith(new GsonDeserializer<>(gson))
                        .setSigningKey(new AuthService().getAccessTokenSecret())
                        .build()
                        .parseClaimsJws(access_token)
                        .getBody();
                String account_id = claims.get("id", String.class);
                req.setAttribute("account_id", Integer.parseInt(account_id));
                return true;
            } catch (JwtException e) {
                System.out.println(e);
            }
        }
        return false;
    }

    public static boolean isAdmin(HttpServletRequest req) {
        String reqHeader = req.getHeader("authorization");
        if (reqHeader != null && reqHeader.startsWith("Bearer ") == true) {
            String access_token = reqHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .deserializeJsonWith(new GsonDeserializer<>(gson))
                        .setSigningKey(new AuthService().getAccessTokenSecret())
                        .build()
                        .parseClaimsJws(access_token)
                        .getBody();
                String account_id = claims.get("id", String.class);
                String account_role = claims.get("role", String.class);
                if (account_role.equals("Admin")) {
                    req.setAttribute("account_id", Integer.parseInt(account_id));
                    return true;
                }
            } catch (JwtException e) {
                System.out.println(e);
            }
        }
        return false;
    }

    public static boolean isCustomer(HttpServletRequest req) {
        String reqHeader = req.getHeader("authorization");
        if (reqHeader != null && reqHeader.startsWith("Bearer ") == true) {
            String access_token = reqHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .deserializeJsonWith(new GsonDeserializer<>(gson))
                        .setSigningKey(new AuthService().getAccessTokenSecret())
                        .build()
                        .parseClaimsJws(access_token)
                        .getBody();
                String account_id = claims.get("id", String.class);
                String account_role = claims.get("role", String.class);
                if (account_role.equals("Customer")) {
                    req.setAttribute("account_id", Integer.parseInt(account_id));
                    return true;
                }
            } catch (JwtException e) {
                System.out.println(e);
            }
        }
        return false;
    }
}
