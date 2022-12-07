package com.api.shoesshop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.shoesshop.entities.ProductVariant;

public interface ProductDetailRepository extends JpaRepository<ProductVariant, Long> {
    // Page<ProductVariant> findBySkuContaining(String sku, Pageable pageable);

    Page<ProductVariant> findByInventory(int inventory, Pageable pageable);

    Page<ProductVariant> findByProduct_NameContaining(String productName, Pageable pageable);

    Page<ProductVariant> findByVariantValues_ValueContaining(String value, Pageable pageable);
}
