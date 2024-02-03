package com.market.your_broker;

public class User {
    public String username, email, password, mobile,image,onSignlID;

    public User() {
    }

    public User(String name, String password, String email, String mobile) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.onSignlID=onSignlID;
    }
    public User(String name, String password, String email, String mobile, String onSignlID, String image) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.image = image;
        this.onSignlID=onSignlID;
    }
    private String imageUrl;
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
