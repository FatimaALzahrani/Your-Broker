package com.market.your_broker;

public class Product {
    String userName;
    String name;
    String image;

    public String getOrprice() {
        return Orprice;
    }

    public void setOrprice(String orprice) {
        Orprice = orprice;
    }

    String Orprice;

    String description;
    String price;
    String email;
    String id;
    String image2;
    String date;
    String num;



    public Product(String userName, String name, String image, String description, String price, String email, String id,   String num) {
        this.userName = userName;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.email = email;
        this.id = id;
        this.image2 = image2;
        this.date = date;
        this.num = num;
    }
    public Product() {
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
