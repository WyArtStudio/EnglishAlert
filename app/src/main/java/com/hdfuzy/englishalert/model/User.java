package com.hdfuzy.englishalert.model;

public class User {
    private String email;
    private String imgUrl;
    private String password;
    private String userName;

    public User(String email, String password, String userName, String imgUrl) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.imgUrl = imgUrl;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
