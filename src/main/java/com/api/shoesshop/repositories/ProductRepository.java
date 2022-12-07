package com.api.shoesshop.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.shoesshop.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
        Page<Product> findByIdIn(List<Long> ids, Pageable pageable);

}
