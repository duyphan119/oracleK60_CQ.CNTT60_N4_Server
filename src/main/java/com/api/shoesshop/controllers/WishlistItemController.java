package com.api.shoesshop.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.shoesshop.entities.WishlistItem;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.services.WishlistItemService;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class WishlistItemController {

    @Autowired
    private WishlistItemService wishlistItemService;

    @GetMapping("/api/wishlist/account")
    public ResponseEntity<String> findByAccountId(HttpServletRequest req, @RequestParam Map<String, String> query) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            long accountId = Long.parseLong(req.getAttribute("account_id").toString());
            if (query != null) {
                Page<WishlistItem> page = wishlistItemService.findByAccountId(accountId, query);
                return Helper.responseSuccess(new FindAll<>(page.getContent(), page.getTotalElements()));
            } else {
                List<WishlistItem> list = wishlistItemService.findAllByAccountId(accountId);
                return Helper.responseSuccess(new FindAll<>(list, list.size()));
            }
        }
        return Helper.responseUnauthorized();
    }

    @PostMapping("/api/wishlist")
    public ResponseEntity<String> save(HttpServletRequest req, @RequestBody WishlistItem body) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                long accountId = Long.parseLong(req.getAttribute("account_id").toString());
                body.setAccountId(accountId);
                WishlistItem wishlistItem = wishlistItemService.save(body);
                return Helper.responseCreated(wishlistItem);
            } catch (Exception e) {
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();

    }

    @DeleteMapping("/api/wishlist/{id}")
    public ResponseEntity<String> save(HttpServletRequest req, @PathVariable long id) {
        if (AuthInterceptor.isLoggedin(req) == true) {
            try {
                wishlistItemService.delete(id);
                return Helper.responseSuccessNoData();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return Helper.responseUnauthorized();
    }
}
