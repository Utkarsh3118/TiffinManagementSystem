package com.USRT.tiffinmanagementsystem.Model;

public class PlaceOrder {

    String order_id,cart_id,menu_id,user_id,vendor_id,item_name,price,content,quantity,total_price,type,image,date,time,note,delivery_status;

    public PlaceOrder() {
    }

    public PlaceOrder(String order_id, String cart_id, String menu_id, String user_id, String vendor_id, String item_name, String price, String content, String quantity, String total_price, String type, String image, String date, String time, String note, String delivery_status) {
        this.order_id = order_id;
        this.cart_id = cart_id;
        this.menu_id = menu_id;
        this.user_id = user_id;
        this.vendor_id = vendor_id;
        this.item_name = item_name;
        this.price = price;
        this.content = content;
        this.quantity = quantity;
        this.total_price = total_price;
        this.type = type;
        this.image = image;
        this.date = date;
        this.time = time;
        this.note = note;
        this.delivery_status = delivery_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }
}
