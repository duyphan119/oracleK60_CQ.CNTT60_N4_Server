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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.shoesshop.entities.ProductCategory;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.services.ProductCategoryService;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/api/product-category")
    public ResponseEntity<String> findAll(@RequestParam Map<String, String> query) {
        try {
            productCategoryService.findAll(query);
            if (query != null) {

                Page<ProductCategory> page = productCategoryService.findAll(query);
                return Helper.responseSuccess(new FindAll<>(page.getContent(), page.getTotalElements()));
            } else {
                List<ProductCategory> list = productCategoryService.findAll2(query);
                return Helper.responseSuccess(new FindAll<>(list, list.size()));
            }
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();

        }
    }

    @GetMapping("/api/product-category/all")
    public ResponseEntity<String> findAll(HttpServletRequest req) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                List<ProductCategory> list = productCategoryService.findAll();
                return Helper.responseSuccess(list);
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();

            }
        }
        return Helper.responseUnauthorized();

    }

    @GetMapping("/api/product-category/{id}")
    public ResponseEntity<String> findById(@PathVariable long id) {
        try {
            ProductCategory Productcategory = productCategoryService.findById(id);
            return Helper.responseSuccess(Productcategory);
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();

        }
    }

    @PostMapping("/api/product-category")
    public ResponseEntity<String> save(HttpServletRequest req, @RequestBody ProductCategory body) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                ProductCategory createdProductCategory = productCategoryService.save(body);
                if (createdProductCategory != null) {
                    return Helper.responseCreated(createdProductCategory);
                }
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
            return Helper.responseError();
        }
        return Helper.responseUnauthorized();

    }

    @PatchMapping("/api/product-category/{id}")
    public ResponseEntity<String> update(HttpServletRequest req, @PathVariable long id,
            @RequestBody ProductCategory body) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                ProductCategory createdProductCategory = productCategoryService.update(id, body);
                if (createdProductCategory != null) {
                    return Helper.responseCreated(createdProductCategory);
                }
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
            return Helper.responseError();
        }
        return Helper.responseUnauthorized();
    }

    @DeleteMapping("/api/product-category/{id}")
    public ResponseEntity<String> delete(HttpServletRequest req, @PathVariable long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                productCategoryService.delete(id);
                return Helper.responseSuccessNoData();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();

    }

}
