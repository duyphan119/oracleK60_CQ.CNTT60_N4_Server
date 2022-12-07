package com.api.shoesshop.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.api.shoesshop.entities.Variant;

public interface VariantService {
    Page<Variant> findAll(Map<String, String> query);

    List<Variant> search(String q);

    Optional<Variant> findById(long id);

    Variant save(Variant variant);

    Variant update(Variant variant, long id);

    void delete(long id);

    long count();

}
