package com.api.shoesshop.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.shoesshop.entities.VariantValue;
import com.api.shoesshop.repositories.VariantValueRepository;
import com.api.shoesshop.services.VariantValueService;
import com.api.shoesshop.utils.Helper;

@Service
public class VariantValueServiceImpl implements VariantValueService {

    @Autowired
    private VariantValueRepository variantValueRepository;

    @Override
    public Page<VariantValue> findAll(Map<String, String> query) {
        Pageable pageable = Helper.getPageable(query);
        return variantValueRepository.findAll(pageable);
    }

    @Override
    public List<VariantValue> findAll() {
        return variantValueRepository.findAll();
    }

    @Override
    public List<VariantValue> search(String q) {
        return null;
    }

    @Override
    public Optional<VariantValue> findById(long id) {
        return variantValueRepository.findById(id);
    }

    @Override
    public VariantValue save(VariantValue variantValue) {
        return variantValueRepository.save(variantValue);
    }

    @Override
    public VariantValue update(VariantValue variantValue, long id) {
        VariantValue exiVariantValue = variantValueRepository.findById(id).get();
        if (exiVariantValue != null) {
            exiVariantValue.setValue(variantValue.getValue());
            exiVariantValue.setVariantId(variantValue.getVariantId());
        }
        return variantValueRepository.save(exiVariantValue);
    }

    @Override
    public void delete(long id) {
        variantValueRepository.deleteById(id);
    }

    @Override
    public long count() {
        return variantValueRepository.count();
    }

}
