package com.api.shoesshop.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api.shoesshop.services.AuthService;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.gson.io.GsonDeserializer;

@Component
public class AdminInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                        throws Exception {
                String reqHeader = request.getHeader("authorization");
                if (reqHeader != null && reqHeader.startsWith("Bearer ") == true) {
                        String access_token = reqHeader.substring(7);
                        try {
                                Claims claims = Jwts.parserBuilder()
                                                .deserializeJsonWith(new GsonDeserializer<>(new Gson()))
                                                .setSigningKey(new AuthService().getAccessTokenSecret())
                                                .build()
                                                .parseClaimsJws(access_token)
                                                .getBody();
                                String account_id = claims.get("id", String.class);
                                String account_role = claims.get("role", String.class);
                                if (account_role.equals("Admin")) {
                                        request.setAttribute("account_id", Integer.parseInt(account_id));
                                        return true;
                                }
                        } catch (JwtException e) {
                                System.out.println(e);
                        }
                }
                System.out.print("log here ---- Is admin");
                return true;
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                        org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {

        }

        @Override
        public void afterCompletion(HttpServletRequest request,
                        HttpServletResponse response, Object handler, Exception ex)
                        throws Exception {
        }
}
