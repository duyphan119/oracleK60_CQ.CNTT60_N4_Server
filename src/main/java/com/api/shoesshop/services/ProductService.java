package com.api.shoesshop.services;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.api.shoesshop.entities.Product;

public interface ProductService {
    Page<Product> findAll(Map<String, String> query);

    Page<Product> search(Map<String, String> query);

    Optional<Product> findById(long id);

    Product save(Product product);

    Product update(Product product, long id);

    void delete(long id);

    long count();

    Page<Product> recommend(Map<String, String> query);

}
