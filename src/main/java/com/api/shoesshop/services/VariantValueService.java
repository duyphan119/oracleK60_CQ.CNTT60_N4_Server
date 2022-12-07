package com.api.shoesshop.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.api.shoesshop.entities.VariantValue;

public interface VariantValueService {
    Page<VariantValue> findAll(Map<String, String> query);

    List<VariantValue> findAll();

    List<VariantValue> search(String q);

    Optional<VariantValue> findById(long id);

    VariantValue save(VariantValue variantValue);

    VariantValue update(VariantValue variantValue, long id);

    void delete(long id);

    long count();
}
