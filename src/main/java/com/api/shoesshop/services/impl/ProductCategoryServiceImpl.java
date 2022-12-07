package com.api.shoesshop.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.shoesshop.entities.ProductCategory;
import com.api.shoesshop.repositories.ProductCategoryRepository;
import com.api.shoesshop.services.ProductCategoryService;
import com.api.shoesshop.utils.Helper;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory save(ProductCategory entity) {
        return productCategoryRepository.save(entity);
    }

    @Override
    public Page<ProductCategory> findAll(Map<String, String> query) {
        String name = query.get("name");
        String alias = query.get("alias");

        Pageable pageable = Helper.getPageable(query);

        if (name != null) {
            return productCategoryRepository.findByNameContaining(name, pageable);
        }
        if (alias != null) {
            return productCategoryRepository.findByAliasContaining(alias, pageable);
        }

        return productCategoryRepository.findAll(pageable);

    }

    @Override
    public List<ProductCategory> findAll2(Map<String, String> query) {

        return productCategoryRepository.findAll();

    }

    @Override
    public void delete(Long id) {
        productCategoryRepository.deleteById(id);
    }

    @Override
    public ProductCategory update(long id, ProductCategory entity) {
        ProductCategory Productcategory = findById(id);
        if (Productcategory != null) {
            entity.setId(id);
            return productCategoryRepository.save(entity);
        }
        return null;
    }

    @Override
    public ProductCategory findById(long id) {
        Optional<ProductCategory> optional = productCategoryRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }

}
