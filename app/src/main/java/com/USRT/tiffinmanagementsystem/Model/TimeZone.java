package com.USRT.tiffinmanagementsystem.Model;

public class TimeZone {

    String timezoneid,vendor_id,lunchtime,dinnertime;

    public TimeZone() {
    }

    public TimeZone(String timezoneid, String vendor_id, String lunchtime, String dinnertime) {
        this.timezoneid = timezoneid;
        this.vendor_id = vendor_id;
        this.lunchtime = lunchtime;
        this.dinnertime = dinnertime;
    }

    public String getTimezoneid() {
        return timezoneid;
    }

    public void setTimezoneid(String timezoneid) {
        this.timezoneid = timezoneid;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
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
}
