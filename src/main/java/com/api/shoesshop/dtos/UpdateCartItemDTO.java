package com.api.shoesshop.dtos;

public class UpdateCartItemDTO {
    Integer newQuantity;

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }
}
