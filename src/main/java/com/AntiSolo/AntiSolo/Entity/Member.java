package com.AntiSolo.AntiSolo.Entity;
public class Member {
    private String name;
    private String imageUrl;

    public Member() {} // Default constructor for MongoDB

    public Member(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
