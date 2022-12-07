package com.api.shoesshop.services;

import java.util.Map;
import java.util.List;

import org.springframework.data.domain.Page;

import com.api.shoesshop.entities.ProductCategory;

public interface ProductCategoryService {
    ProductCategory save(ProductCategory entity);

    Page<ProductCategory> findAll(Map<String, String> query);

    List<ProductCategory> findAll2(Map<String, String> query);

    void delete(Long id);

    ProductCategory update(long id, ProductCategory entity);

    ProductCategory findById(long id);

    List<ProductCategory> findAll();
}
