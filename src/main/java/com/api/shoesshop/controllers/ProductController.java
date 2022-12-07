package com.api.shoesshop.controllers;

import java.util.Map;
import java.util.Optional;

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

import com.api.shoesshop.entities.Product;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.services.ProductService;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/api/product")
    public ResponseEntity<String> findAll(@RequestParam Map<String, String> query) {
        try {
            // if (query.get("category_alias") != null) {
            // Page<ProductCategory> page = productCategoryService.findAll(query,
            // variantValueIds);
            // List<Product> products = new ArrayList<>();
            // for (int i = 0; i < page.getContent().size(); i++) {
            // products.add(page.getContent().get(i).getProduct());
            // }
            // return Helper.responseSuccess(new FindAll<>(products,
            // page.getTotalElements()));
            // }
            Page<Product> page = query.get("q") != null ? productService.search(query)
                    : productService.findAll(query);
            return Helper.responseSuccess(new FindAll<>(page.getContent(), page.getTotalElements()));
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }
    }

    @GetMapping(value = "/api/product/recommend")
    public ResponseEntity<String> recommend(@RequestParam Map<String, String> query) {
        try {

            Page<Product> page = productService.recommend(query);
            return Helper.responseSuccess(new FindAll<>(page.getContent(), page.getTotalElements()));
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }
    }

    @GetMapping(value = "/api/product/{id}")
    public ResponseEntity<String> findById(@PathVariable(name = "id") long id) {
        try {
            Optional<Product> Product = productService.findById(id);
            return Helper.responseSuccess(Product.get());
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }
    }

    @PostMapping(value = "/api/product")
    public ResponseEntity<String> save(HttpServletRequest req, @RequestBody Product body) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                return Helper.responseCreated(productService.save(body));
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @PatchMapping(value = "/api/product/{id}")
    public ResponseEntity<String> update(HttpServletRequest req, @RequestBody Product body,
            @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                return Helper.responseSuccess(productService.update(body, id));
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @DeleteMapping(value = "/api/product/{id}")
    public ResponseEntity<String> delete(HttpServletRequest req, @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                productService.delete(id);
                return Helper.responseSuccessNoData();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

}
