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

import com.api.shoesshop.entities.Variant;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.services.VariantService;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @GetMapping(value = "/api/variant")
    public ResponseEntity<String> findAll(@RequestParam Map<String, String> query) {
        try {
            Page<Variant> page = variantService.findAll(query);
            return Helper.responseSuccess(new FindAll<>(page.getContent(), page.getTotalElements()));
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }

    }

    @GetMapping(value = "/api/variant/{id}")
    public ResponseEntity<String> findById(@PathVariable(name = "id") long id) {
        try {
            Optional<Variant> variant = variantService.findById(id);
            return Helper.responseSuccess(variant.get());
        } catch (Exception e) {
            System.out.println(e);
            return Helper.responseError();
        }
    }

    @PostMapping(value = "/api/variant")
    public ResponseEntity<String> save(HttpServletRequest req, @RequestBody Variant body) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                return Helper.responseCreated(variantService.save(body));
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @PatchMapping(value = "/api/variant/{id}")
    public ResponseEntity<String> update(HttpServletRequest req, @RequestBody Variant body,
            @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                return Helper.responseSuccess(variantService.update(body, id));
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

    @DeleteMapping(value = "/api/variant/{id}")
    public ResponseEntity<String> delete(HttpServletRequest req, @PathVariable(name = "id") long id) {
        if (AuthInterceptor.isAdmin(req) == true) {
            try {
                variantService.delete(id);
                return Helper.responseSuccessNoData();
            } catch (Exception e) {
                System.out.println(e);
                return Helper.responseError();
            }
        }
        return Helper.responseUnauthorized();
    }

}
