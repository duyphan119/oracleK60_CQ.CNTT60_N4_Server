package com.api.shoesshop.controllers;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.shoesshop.dtos.UpdateCartItemDTO;
import com.api.shoesshop.entities.Order;
import com.api.shoesshop.entities.OrderItem;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.repositories.OrderItemRepository;
import com.api.shoesshop.repositories.OrderRepository;
import com.api.shoesshop.utils.Helper;

@Controller

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CartController {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @GetMapping("/api/cart/account")
    public ResponseEntity<String> findCartByAccount(HttpServletRequest req) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                long accountId = Long.parseLong(req.getAttribute("account_id").toString());
                Order order = orderRepository.findByStatusAndAccountId(null, accountId);
                return Helper.responseSuccess(order);
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @PostMapping("/api/cart/item")
    public ResponseEntity<String> addToCart(HttpServletRequest req, @RequestBody OrderItem item) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                long accountId = Long.parseLong(req.getAttribute("account_id").toString());
                Order order = orderRepository.findByStatusAndAccountId(null, accountId);
                if (order == null) {
                    order = new Order();
                    order.setAccountId(accountId);
                    order.setCreatedAt(new Date());
                    order = orderRepository.save(order);
                }
                item.setOrderId(order.getId());
                OrderItem checkItem = orderItemRepository.findByOrderIdAndProductVariantId(item.getOrderId(),
                        item.getProductVariantId());
                if (checkItem == null) {
                    checkItem = item;
                } else {
                    checkItem.setPrice(checkItem.getProductVariant().getProduct().getSalePrice());
                    checkItem.setQuantity(checkItem.getQuantity() + item.getQuantity());
                }
                return Helper
                        .responseSuccess(
                                orderItemRepository.findById(orderItemRepository.save(checkItem).getId()).get());
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @PatchMapping("/api/cart/item/{id}")
    public ResponseEntity<String> updateCartItem(HttpServletRequest req, @RequestBody UpdateCartItemDTO dto,
            @PathVariable(name = "id") Long id) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                Optional<OrderItem> optional = orderItemRepository.findById(id);
                if (optional.isPresent() == true) {
                    OrderItem orderItem = optional.get();
                    orderItem.setQuantity(dto.getNewQuantity());
                    return Helper.responseSuccess(orderItemRepository.save(orderItem));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return Helper.responseError();
        }
        return Helper.responseUnauthorized();
    }

    @DeleteMapping("/api/cart/item/{id}")
    public ResponseEntity<String> deleteCartItem(HttpServletRequest req, @PathVariable(name = "id") Long id) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                orderItemRepository.deleteById(id);
                return Helper.responseSuccessNoData();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }
}
