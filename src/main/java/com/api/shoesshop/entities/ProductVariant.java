package com.api.shoesshop.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue
    @Column(name = "product_variant_id")
    private Long id;

    @Column(name = "product_id_pk", nullable = false)
    private long productId;

    @Column(name = "inventory", nullable = false)
    private int inventory;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id_pk", referencedColumnName = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(name = "product_variant_details", joinColumns = {
            @JoinColumn(name = "product_variant_id_pk") }, inverseJoinColumns = {
                    @JoinColumn(name = "variant_value_id_pk") })
    Set<VariantValue> variantValues = new HashSet<>();

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return this.productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getInventory() {
        return this.inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<VariantValue> getVariantValues() {
        return variantValues;
    }

    public void setVariantValues(Set<VariantValue> variantValues) {
        this.variantValues = variantValues;
    }

}
