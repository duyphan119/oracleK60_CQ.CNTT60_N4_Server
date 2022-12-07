package com.api.shoesshop.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    // @Column(name = "province")
    // private String province;

    // @Column(name = "district")
    // private String district;

    // @Column(name = "ward")
    // private String ward;

    @Column(name = "address")
    private String address;

    @Column(name = "shipping_price")
    private int shippingPrice;

    @Column(name = "coupon_id_pk", nullable = true)
    private Long couponId;

    @Column(name = "account_id_pk", nullable = false)
    private Long accountId;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Account.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id_pk", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Coupon.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id_pk", referencedColumnName = "coupon_id", insertable = false, updatable = false)
    private Coupon coupon;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id_pk")
    private Set<OrderItem> items = new HashSet<>();

    @Column(name = "created_at", nullable = true)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "order_status")
    private String status;

    @Column(name = "payment_method")
    private String paymentMethod;

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    // @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    // @JoinTable(name = "order_coupons", joinColumns = {
    // @JoinColumn(name = "order_id_pk") }, inverseJoinColumns = { @JoinColumn(name
    // = "coupon_id_pk") })
    // List<Coupon> coupons = new ArrayList<>();

    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // public String getProvince() {
    // return this.province;
    // }

    // public void setProvince(String province) {
    // this.province = province;
    // }

    // public String getDistrict() {
    // return this.district;
    // }

    // public void setDistrict(String district) {
    // this.district = district;
    // }

    // public String getWard() {
    // return this.ward;
    // }

    // public void setWard(String ward) {
    // this.ward = ward;
    // }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getShippingPrice() {
        return this.shippingPrice;
    }

    public void setShippingPrice(int shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Set<OrderItem> getItems() {
        return this.items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullName;
    }
}
