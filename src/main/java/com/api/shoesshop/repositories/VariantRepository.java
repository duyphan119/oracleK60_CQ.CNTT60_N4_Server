package com.api.shoesshop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.shoesshop.entities.Variant;

public interface VariantRepository extends JpaRepository<Variant, Long> {
    List<Variant> findByNameContaining(String q);
}
