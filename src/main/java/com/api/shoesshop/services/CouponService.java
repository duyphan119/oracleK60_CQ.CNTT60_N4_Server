package com.api.shoesshop.services;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.api.shoesshop.entities.Coupon;

public interface CouponService {
    Coupon save(Coupon entity);

    Coupon update(long id, Coupon entity);

    void delete(Long id);

    Page<Coupon> findAll(Map<String, String> query);

    Coupon findById(long id);

    Coupon check(long accountId, String code);
}
