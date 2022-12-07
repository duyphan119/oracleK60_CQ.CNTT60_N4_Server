package com.api.shoesshop.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.shoesshop.entities.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Page<ProductCategory> findByNameContaining(String name, Pageable pageable);

    Page<ProductCategory> findByAliasContaining(String alias, Pageable pageable);
}
