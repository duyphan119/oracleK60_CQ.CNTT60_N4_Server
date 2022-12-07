package com.api.shoesshop.dtos;

public class RegisterDTO {
    private String email;
    private String password;
    private String full_name;
    private String phone;

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFullName() {
        return this.full_name;
    }

    public String getPhone() {
        return this.phone;
    }

}
