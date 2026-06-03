package com.musicdiary.dtos;

public class SaveSongRequestDTO {

    private String title;

    private String artistName;

    private String imageUrl;

    private String lastFmUrl;

    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastFmUrl() {
        return lastFmUrl;
    }

    public void setLastFmUrl(String lastFmUrl) {
        this.lastFmUrl = lastFmUrl;
    }
}
