package com.hdfuzy.englishalert.model;

public class ScreenItem {
    String Title, Description,  ScreenImg;

    public ScreenItem(String title, String description, String screenImg) {
        Title = title;
        Description = description;
        ScreenImg = screenImg;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getScreenImg() {
        return ScreenImg;
    }

    public void setScreenImg(String screenImg) {
        ScreenImg = screenImg;
    }
}
