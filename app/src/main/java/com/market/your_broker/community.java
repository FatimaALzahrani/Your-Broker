package com.market.your_broker;

public class community {
    String userName;
    String Orprice;
    String name;
    String image;
    String description;

    public String getOrprice() {
        return Orprice;
    }

    public void setOrprice(String orprice) {
        Orprice = orprice;
    }

    String price;
    String email;
    String id;
    String image2;
    String date;
    String num;


    public community() {
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public void setEmail(String email) {
        this.email=email;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setQustions(String qustions) {
        this.name = qustions;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setUserImage(String image2) {
        this.image2=image2;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setNum(String num) {
        this.num = num;
    }


    public String getPrice() {
        return price;
    }
    public String getEmail() {
        return email;
    }
    public String getUserName() {
        return userName;
    }
    public String getQustions() {
        return this.name;
    }
    public String getImage() {
        return image;
    }
    public String getDescription() {
        return description;
    }
    public String getId() {
        return id;
    }
    public String getUserImage(){
        return image2;
    }
    public String getDate() {
        return date;
    }
    public String getNum() {
        return num;
    }
}
