package com.api.shoesshop.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.shoesshop.entities.ProductVariant;
import com.api.shoesshop.repositories.ProductDetailRepository;
import com.api.shoesshop.services.ProductDetailService;
import com.api.shoesshop.utils.Helper;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public Page<ProductVariant> findAll(Map<String, String> query) {
        Pageable pageable = Helper.getPageable(query);
        String sku = query.get("sku");
        String inventory = query.get("inventory");
        String productName = query.get("product_name");
        String variantValue = query.get("variant_value");

        // if (sku != null) {
        // return productDetailRepository.findBySkuContaining(sku, pageable);
        // }
        if (inventory != null) {
            return productDetailRepository.findByInventory(Integer.parseInt(inventory), pageable);
        }
        if (productName != null) {
            return productDetailRepository.findByProduct_NameContaining(productName, pageable);
        }
        if (variantValue != null) {
            return productDetailRepository.findByVariantValues_ValueContaining(variantValue, pageable);
        }

        return productDetailRepository.findAll(pageable);
    }

    @Override
    public List<ProductVariant> search(String q) {

        return null;
    }

    @Override
    public Optional<ProductVariant> findById(long id) {
        return productDetailRepository.findById(id);
    }

    @Override
    public ProductVariant save(ProductVariant productDetail) {
        return productDetailRepository.save(productDetail);
    }

    @Override
    public ProductVariant update(ProductVariant productDetail, long id) {
        ProductVariant exiProductDetail = productDetailRepository.findById(id).get();
        if (exiProductDetail != null) {
            // exiProductDetail.setSku(productDetail.getSku());
            // exiProductDetail.setThumbnail(productDetail.getThumbnail());
            exiProductDetail.setInventory(productDetail.getInventory());
            exiProductDetail.setProductId(productDetail.getProductId());
            exiProductDetail.setVariantValues(productDetail.getVariantValues());
            // exiProductDetail.setWeight(productDetail.getWeight());
        }
        return productDetailRepository.save(exiProductDetail);
    }

    @Override
    public void delete(long id) {

        productDetailRepository.deleteById(id);
    }

    @Override
    public long count() {

        return productDetailRepository.count();
    }

    @Override
    public List<ProductVariant> saveMany(Iterable<ProductVariant> entities) {
        return productDetailRepository.saveAll(entities);
    }
}
