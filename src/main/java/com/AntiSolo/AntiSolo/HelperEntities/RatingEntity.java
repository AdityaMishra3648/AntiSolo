package com.AntiSolo.AntiSolo.HelperEntities;

public class RatingEntity {
    private String userName;
    private int rating;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRating() {
        return rating;
    }

    public RatingEntity(String userName, int rating) {
        this.userName = userName;
        this.rating = rating;
    }

    public RatingEntity() {
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
