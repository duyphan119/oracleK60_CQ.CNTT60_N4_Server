package com.api.shoesshop.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "variants")
public class Variant {
    @Id
    @GeneratedValue
    @Column(name = "variant_id")
    private Long id;

    @Column(name = "variant_name", nullable = false)
    private String name;

    // @OneToMany(mappedBy = "variant", fetch = FetchType.EAGER)
    // @JsonIgnoreProperties({ "variant" })
    // private Set<VariantValue> variantValues = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // public Set<VariantValue> getVariantValues() {
    // return variantValues;
    // }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public void setVariantValues(Set<VariantValue> variantValues) {
    // this.variantValues = variantValues;
    // }
}
