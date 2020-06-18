package com.USRT.tiffinmanagementsystem.Model;

public class Feedback {

    String feed_id,user_id,comment;
    float rate_val;

    public Feedback() {
    }

    public Feedback(String feed_id, String user_id, String comment, float rate_val) {
        this.feed_id = feed_id;
        this.user_id = user_id;
        this.comment = comment;
        this.rate_val = rate_val;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRate_val() {
        return rate_val;
    }

    public void setRate_val(float rate_val) {
        this.rate_val = rate_val;
    }
}
