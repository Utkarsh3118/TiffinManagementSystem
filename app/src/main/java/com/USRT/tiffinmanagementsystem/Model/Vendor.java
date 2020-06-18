package com.USRT.tiffinmanagementsystem.Model;

public class Vendor {

    String vendor_id,vname,vphone,vemail,vpassword,vaddress,vage,location,photo;
    double latitude,longitude;
String lunchtime,dinnertime;
    public Vendor() {
    }

    public Vendor(String vendor_id, String vname, String vphone, String vemail, String vpassword, String vaddress, String vage, String location, double latitude, double longitude, String photo) {
        this.vendor_id = vendor_id;
        this.vname = vname;
        this.vphone = vphone;
        this.vemail = vemail;
        this.vpassword = vpassword;
        this.vaddress = vaddress;
        this.vage = vage;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
    }

    public Vendor(String vendor_id, String vname, String vphone, String vemail, String vpassword, String vaddress, String vage, String location, double latitude, double longitude, String photo, String lunchtime, String dinnertime) {
        this.vendor_id = vendor_id;
        this.vname = vname;
        this.vphone = vphone;
        this.vemail = vemail;
        this.vpassword = vpassword;
        this.vaddress = vaddress;
        this.vage = vage;
        this.location = location;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lunchtime = lunchtime;
        this.dinnertime = dinnertime;
    }

    public String getLunchtime() {
        return lunchtime;
    }

    public void setLunchtime(String lunchtime) {
        this.lunchtime = lunchtime;
    }

    public String getDinnertime() {
        return dinnertime;
    }

    public void setDinnertime(String dinnertime) {
        this.dinnertime = dinnertime;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVphone() {
        return vphone;
    }

    public void setVphone(String vphone) {
        this.vphone = vphone;
    }

    public String getVemail() {
        return vemail;
    }

    public void setVemail(String vemail) {
        this.vemail = vemail;
    }

    public String getVpassword() {
        return vpassword;
    }

    public void setVpassword(String vpassword) {
        this.vpassword = vpassword;
    }

    public String getVaddress() {
        return vaddress;
    }

    public void setVaddress(String vaddress) {
        this.vaddress = vaddress;
    }

    public String getVage() {
        return vage;
    }

    public void setVage(String vage) {
        this.vage = vage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
