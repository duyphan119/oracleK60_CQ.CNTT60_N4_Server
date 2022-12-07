package com.api.shoesshop.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.api.shoesshop.entities.Product;
import com.api.shoesshop.entities.ProductCategory;
import com.api.shoesshop.repositories.ProductCategoryRepository;
import com.api.shoesshop.repositories.ProductRepository;
import com.api.shoesshop.services.ProductService;
import com.api.shoesshop.utils.Helper;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public Page<Product> findAll(Map<String, String> query) {
        Pageable pageable = Helper.getPageable(query);
        String name = query.get("name");
        String alias = query.get("alias");
        String categoryAlias = query.get("category_alias");
        // String price = query.get("price");
        // String salePrice = query.get("sale_price");
        String minPrice = query.get("min_price");
        String maxPrice = query.get("max_price");
        String size = query.get("size");
        String baseSql = "select distinct p.product_id from products p, product_variants pv, product_categories pc,"
                + "product_variant_details pvd, variant_values vv where p.product_id = pv.product_id_pk "
                + " and pc.category_id = p.product_category_id_pk  "
                + " and pvd.product_variant_id_pk = pv.product_variant_id and pvd.variant_value_id_pk = vv.variant_value_id ";
        String sql = baseSql;
        if (categoryAlias != null) {
            sql += (" and pc.category_alias='" + categoryAlias + "' ");
        }
        if (minPrice != null) {
            sql += (" and p.sale_price >=" + minPrice);
        }
        if (maxPrice != null) {
            sql += (" and p.sale_price <=" + maxPrice);
        }
        if (alias != null) {
            sql += (" and p.product_alias ='" + alias + "'");
        }
        if (size != null) {
            sql += (" and vv.value ='" + size + "'");
        }
        if (name != null) {
            sql += (" and p.product_name like '%" + name + "%'");
        }
        if (sql.equals(baseSql) == false) {
            List<Long> ids = jdbcTemplate.query(sql, new RowMapper<Long>() {
                public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getLong("product_id");
                }
            });
            return productRepository.findByIdIn(ids, pageable);
        }

        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> search(Map<String, String> query) {
        Pageable pageable = Helper.getPageable(query);
        String q = query.get("q");
        String sql = "select p.product_id from products p, product_variants pv, product_categories pc, categories c,"
                + "product_variant_details pvd, variant_values vv where p.product_id = pv.product_id_pk "
                + " and pc.category_id_pk = c.category_id and pc.product_id_pk = p.product_id  "
                + " and pvd.product_variant_id_pk = pv.product_variant_id and pvd.variant_value_id_pk = vv.variant_value_id and ("
                + " p.product_name like '%" + q + "%'"
                + " or p.product_alias like '%" + q + "%'"
                + " or c.category_name like '%" + q + "%'"
                + " or c.category_alias like '%" + q + "%'"
                + " or vv.value like '%" + q + "%'"
                + ")";

        if (sql.equals("") == false) {
            List<Long> ids = jdbcTemplate.query(sql, new RowMapper<Long>() {
                public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getLong("product_id");
                }
            });
            return productRepository.findByIdIn(ids, pageable);
        }

        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> recommend(Map<String, String> query) {
        Pageable pageable = Helper.getPageable(query);
        String alias = query.get("alias");
        String sql = "select p.product_id from products p, product_categories pc where p.product_id = pc.product_id_pk and pc.category_id_pk in (select pc.category_id_pk from products p, product_categories pc where p.product_id = pc.product_id_pk and p.product_alias = '"
                + alias + "') and p.product_alias != '" + alias + "'";

        if (sql.equals("") == false) {
            List<Long> ids = jdbcTemplate.query(sql, new RowMapper<Long>() {
                public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getLong("product_id");
                }
            });
            return productRepository.findByIdIn(ids, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product, long id) {
        Product exiProduct = productRepository.findById(id).get();
        if (exiProduct != null) {
            exiProduct.setName(product.getName());
            exiProduct.setAlias(product.getAlias());
            exiProduct.setThumbnail(product.getThumbnail());
            exiProduct.setProductCategoryId(product.getProductCategoryId());
            Optional<ProductCategory> optional = productCategoryRepository.findById(product.getProductCategoryId());
            if (optional.isPresent())
                exiProduct.setProductCategory(optional.get());

        }
        return productRepository.save(exiProduct);
    }

    @Override
    public void delete(long id) {

        productRepository.deleteById(id);
    }

    @Override
    public long count() {

        return productRepository.count();
    }

}
