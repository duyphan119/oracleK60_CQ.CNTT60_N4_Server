package com.api.shoesshop.controllers;

import java.util.List;
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

import com.api.shoesshop.entities.VariantValue;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.services.VariantValueService;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class VariantValueController {

        @Autowired
        private VariantValueService variantValueService;

        @GetMapping("/api/variant-value")
        public ResponseEntity<String> findAll(
                        @RequestParam Map<String, String> query) {
                try {
                        Page<VariantValue> page = variantValueService.findAll(query);
                        return Helper.responseSuccess(new FindAll<>(page.getContent(), page.getTotalElements()));
                } catch (Exception e) {
                        System.out.println(e);
                        return Helper.responseError();
                }

        }

        @GetMapping("/api/variant-value/all")
        public ResponseEntity<String> findAll() {
                try {
                        List<VariantValue> list = variantValueService.findAll();
                        return Helper.responseSuccess(list);
                } catch (Exception e) {
                        System.out.println(e);
                        return Helper.responseError();
                }

        }

        @GetMapping(value = "/api/variant-value/{id}")
        public ResponseEntity<String> findById(@PathVariable(name = "id") long id) {
                try {
                        Optional<VariantValue> variantValue = variantValueService.findById(id);
                        return Helper.responseSuccess(variantValue.get());
                } catch (Exception e) {
                        System.out.println(e);
                        return Helper.responseError();
                }
        }

        @PostMapping(value = "/api/variant-value")
        public ResponseEntity<String> save(HttpServletRequest req, @RequestBody VariantValue body) {
                if (AuthInterceptor.isAdmin(req) == true) {
                        try {
                                return Helper.responseCreated(variantValueService.save(body));
                        } catch (Exception e) {
                                System.out.println(e);
                                return Helper.responseError();
                        }
                }
                return Helper.responseUnauthorized();
        }

        @PatchMapping(value = "/api/variant-value/{id}")
        public ResponseEntity<String> update(HttpServletRequest req, @RequestBody VariantValue body,
                        @PathVariable(name = "id") long id) {
                if (AuthInterceptor.isAdmin(req) == true) {
                        try {
                                return Helper.responseSuccess(variantValueService.update(body, id));
                        } catch (Exception e) {
                                System.out.println(e);
                                return Helper.responseError();
                        }
                }
                return Helper.responseUnauthorized();
        }

        @DeleteMapping(value = "/api/variant-value/{id}")
        public ResponseEntity<String> delete(HttpServletRequest req, @PathVariable(name = "id") long id) {
                if (AuthInterceptor.isAdmin(req) == true) {
                        try {
                                variantValueService.delete(id);
                                return Helper.responseSuccessNoData();
                        } catch (Exception e) {
                                System.out.println(e);
                                return Helper.responseError();
                        }
                }
                return Helper.responseUnauthorized();
        }
}
