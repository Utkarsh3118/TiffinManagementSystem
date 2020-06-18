package com.USRT.tiffinmanagementsystem.Model;

public class UserModel {
    String user_id;
    String cust_name;
    String mobile,password;
    String email;
    String address;
    String city;
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel() {

    }

    public UserModel(String user_id, String cust_name, String mobile, String password, String email, String address, String city, String token) {
        this.user_id = user_id;
        this.cust_name = cust_name;
        this.mobile = mobile;
        this.password=password;
        this.email = email;
        this.address = address;
        this.city = city;
        this.token=token;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
