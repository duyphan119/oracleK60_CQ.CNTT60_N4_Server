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
@Table(name = "variant_values")
public class VariantValue {
    @Id
    @GeneratedValue
    @Column(name = "variant_value_id")
    private Long id;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "variant_id_pk", nullable = false)
    private long variantId;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Variant.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id_pk", referencedColumnName = "variant_id", insertable = false, updatable = false)
    private Variant variant;

    // @ManyToMany(mappedBy = "variantValues", fetch = FetchType.EAGER)
    // @JsonIgnoreProperties({ "variantValues" })
    // private Set<ProductDetail> productDetails = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public void setVariantId(long variantId) {
        this.variantId = variantId;
    }

    public long getVariantId() {
        return variantId;
    }

    // public void setProductDetails(Set<ProductDetail> productDetails) {
    // this.productDetails = productDetails;
    // }

    // public Set<ProductDetail> getProductDetails() {
    // return productDetails;
    // }
}
