package com.api.shoesshop.services;

import java.text.ParseException;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.api.shoesshop.entities.Order;

public interface OrderService {
    Page<Order> findAll(Map<String, String> query) throws ParseException;

    Page<Order> findByAccount(long accountId, Map<String, String> query) throws ParseException;

    Order findById(Long id);

    Order save(long accountId, Order order);

    Order updateStatus(Long id, String status);

    void delete(Long id);
}