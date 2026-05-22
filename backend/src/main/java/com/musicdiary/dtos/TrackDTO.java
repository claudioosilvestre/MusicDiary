package com.musicdiary.dtos;

public class TrackDTO {

    private String musicName;

    private String artistName;

    private int totalListeners;

    private String imageURL;

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
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
}
