package com.USRT.tiffinmanagementsystem.Model;

public class Menu {

    String item_id,vendor_id,item_name,price,quantity,photo,type;

    public Menu() {
    }

    public Menu(String item_id, String vendor_id, String item_name, String price, String quantity, String photo, String type) {
        this.item_id = item_id;
        this.vendor_id = vendor_id;
        this.item_name = item_name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;
        this.type = type;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
