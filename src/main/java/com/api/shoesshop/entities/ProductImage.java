package com.api.shoesshop.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue
    @Column(name = "product_image_id")
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "product_id_pk", nullable = false)
    private long productId;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id_pk", referencedColumnName = "product_id", insertable = false, updatable = false)
    private Product product;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public long getProductId() {
        return this.productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
