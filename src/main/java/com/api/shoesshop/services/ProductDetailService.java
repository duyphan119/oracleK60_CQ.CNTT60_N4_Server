package com.api.shoesshop.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.api.shoesshop.entities.ProductVariant;

public interface ProductDetailService {
    Page<ProductVariant> findAll(Map<String, String> query);

    List<ProductVariant> search(String q);

    Optional<ProductVariant> findById(long id);

    ProductVariant save(ProductVariant productDetail);

    List<ProductVariant> saveMany(Iterable<ProductVariant> entities);

    ProductVariant update(ProductVariant productDetail, long id);

    void delete(long id);

    long count();

}
