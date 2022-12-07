package com.api.shoesshop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.shoesshop.entities.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(long productId);
}
