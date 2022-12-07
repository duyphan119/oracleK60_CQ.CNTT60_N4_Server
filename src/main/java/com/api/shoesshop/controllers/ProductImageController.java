package com.api.shoesshop.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.api.shoesshop.entities.ProductImage;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.repositories.ProductImageRepository;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductImageController {

    @Autowired
    ProductImageRepository productImageRepository;

    @GetMapping(value = "/api/product-image")
    public ResponseEntity<String> findAll(@RequestParam Map<String, String> query) {
        try {
            String productId = query.get("productId");
            if (productId != null) {
                List<ProductImage> list = productImageRepository.findByProductId(Long.parseLong(productId));
                return Helper.responseSuccess(new FindAll<>(list, list.size()));

            }
            Pageable pageable = Helper.getPageable(query);
            Page<ProductImage> page = productImageRepository.findAll(pageable);
            return Helper.responseSuccess(new FindAll<>(page.getContent(), page.getTotalElements()));
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }

    }

    @GetMapping(value = "/api/product-image/{id}")
    public ResponseEntity<String> findById(@PathVariable(name = "id") long id) {
        try {
            Optional<ProductImage> optional = productImageRepository.findById(id);
            if (optional.isPresent()) {

                return Helper.responseSuccess(optional.get());
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return Helper.responseError();
    }

    @PostMapping(value = "/api/product-image")
    public ResponseEntity<String> save(HttpServletRequest req, @RequestBody ProductImage body) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                return Helper.responseCreated(productImageRepository.save(body));
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @PostMapping(value = "/api/product-image/many")
    public ResponseEntity<String> save(HttpServletRequest req, @RequestBody Iterable<ProductImage> body) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                return Helper.responseCreated(productImageRepository.saveAll(body));
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @PatchMapping(value = "/api/product-image/{id}")
    public ResponseEntity<String> update(HttpServletRequest req, @RequestBody ProductImage body,
            @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                Optional<ProductImage> optional = productImageRepository.findById(id);
                if (optional.isPresent() == true) {
                    optional.get().setPath(body.getPath());
                    return Helper.responseSuccess(productImageRepository.save(optional.get()));
                }

            } catch (Exception e) {
                System.out.println(e);
            }
            return Helper.responseError();
        }
        return Helper.responseUnauthorized();
    }

    @DeleteMapping(value = "/api/product-image/{id}")
    public ResponseEntity<String> delete(HttpServletRequest req, @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                productImageRepository.deleteById(id);
                return Helper.responseSuccessNoData();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

}
