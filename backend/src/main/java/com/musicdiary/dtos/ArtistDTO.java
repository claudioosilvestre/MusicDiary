package com.musicdiary.dtos;

public class ArtistDTO {

    private String name;

    private int totalListeners;

    private String imageURL;

    private String profileURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalListeners() {
        return totalListeners;
    }

    public void setTotalListeners(int totalListeners) {
        this.totalListeners = totalListeners;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
