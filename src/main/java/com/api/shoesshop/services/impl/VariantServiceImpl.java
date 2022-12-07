package com.api.shoesshop.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.shoesshop.entities.Variant;
import com.api.shoesshop.repositories.VariantRepository;
import com.api.shoesshop.services.VariantService;
import com.api.shoesshop.utils.Helper;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Override
    public void delete(long id) {
        variantRepository.deleteById(id);
    }

    @Override
    public Page<Variant> findAll(Map<String, String> query) {
        Pageable pageable = Helper.getPageable(query);
        return variantRepository.findAll(pageable);
    }

    @Override
    public Optional<Variant> findById(long id) {
        return variantRepository.findById(id);
    }

    @Override
    public Variant save(Variant variant) {
        return variantRepository.save(variant);
    }

    @Override
    public Variant update(Variant variant, long id) {
        Variant exiVariant = variantRepository.findById(id).get();
        if (exiVariant != null) {
            exiVariant.setName(variant.getName());
        }
        return variantRepository.save(exiVariant);
    }

    @Override
    public List<Variant> search(String q) {
        return variantRepository.findByNameContaining(q);
    }

    @Override
    public long count() {

        return variantRepository.count();
    }

}
