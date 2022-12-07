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

import com.api.shoesshop.entities.Coupon;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.services.CouponService;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping(value = "/api/coupon")
    public ResponseEntity<String> findAll(HttpServletRequest req, @RequestParam Map<String, String> query) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                Page<Coupon> page = couponService.findAll(query);
                return Helper.responseSuccess(new FindAll<>(page.getContent(), page.getTotalElements()));
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @GetMapping(value = "/api/coupon/{id}")
    public ResponseEntity<String> findById(HttpServletRequest req, @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                Coupon coupon = couponService.findById(id);
                if (coupon != null) {
                    return Helper.responseSuccess(coupon);
                }
                return Helper.responseError();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @GetMapping(value = "/api/coupon/check")
    public ResponseEntity<String> checkCoupon(HttpServletRequest req, @RequestParam(name = "code") String code) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                long accountId = Long.parseLong(req.getAttribute("account_id").toString());
                Coupon coupon = couponService.check(accountId, code);
                return Helper.responseSuccess(coupon);
            } catch (Exception e) {
                System.out.println(e);
            }
            return Helper.responseError();
        }
        return Helper.responseUnauthorized();
    }

    @PostMapping(value = "/api/coupon")
    public ResponseEntity<String> save(HttpServletRequest req, @RequestBody Coupon body) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                Coupon coupon = couponService.save(body);
                if (coupon != null) {
                    return Helper.responseCreated(coupon);
                }
                return Helper.responseError();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @PatchMapping(value = "/api/coupon/{id}")
    public ResponseEntity<String> update(HttpServletRequest req, @RequestBody Coupon body,
            @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                Coupon coupon = couponService.update(id, body);
                if (coupon != null) {
                    return Helper.responseSuccess(coupon);
                }
                return Helper.responseError();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @DeleteMapping(value = "/api/coupon/{id}")
    public ResponseEntity<String> delete(HttpServletRequest req, @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                couponService.delete(id);
                return Helper.responseSuccessNoData();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

}
