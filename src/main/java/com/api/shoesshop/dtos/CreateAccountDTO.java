package com.api.shoesshop.dtos;

public class CreateAccountDTO {
    private String email;
    private String password;
    private String fullName;
    private String phone;

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getPhone() {
        return this.phone;
    }
}
