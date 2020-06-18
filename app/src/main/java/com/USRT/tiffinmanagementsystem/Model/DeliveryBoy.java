package com.USRT.tiffinmanagementsystem.Model;

public class DeliveryBoy {

    String boyid,vendor_id,name,address,city,mobile,email,password;

    public DeliveryBoy() {
    }

    public DeliveryBoy(String boyid, String vendor_id, String name, String address, String city, String mobile, String email,String password) {
        this.boyid = boyid;
        this.vendor_id = vendor_id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.mobile = mobile;
        this.email = email;
        this.password=password;
    }

    public String getBoyid() {
        return boyid;
    }

    public void setBoyid(String boyid) {
        this.boyid = boyid;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
