package com.musicdiary.dtos;

import java.util.List;

public class DashboardResponseDTO {

    private int totalMusicsSaved;

    private String mostSavedArtist;

    private int timesOfMostSavedArtist;

    private SaveSongResponseDTO lastSavedSong;

    private List<MonthlyCountDTO> monthCountList;

    public int getTotalMusicsSaved() {
        return totalMusicsSaved;
    }

    public void setTotalMusicsSaved(int totalMusicsSaved) {
        this.totalMusicsSaved = totalMusicsSaved;
    }

    public String getMostSavedArtist() {
        return mostSavedArtist;
    }

    public void setMostSavedArtist(String mostSavedArtist) {
        this.mostSavedArtist = mostSavedArtist;
    }

    public int getTimesOfMostSavedArtist() {
        return timesOfMostSavedArtist;
    }

    public void setTimesOfMostSavedArtist(int timesOfMostSavedArtist) {
        this.timesOfMostSavedArtist = timesOfMostSavedArtist;
    }

    public SaveSongResponseDTO getLastSavedSong() {
        return lastSavedSong;
    }

    public void setLastSavedSong(SaveSongResponseDTO lastSavedSong) {
        this.lastSavedSong = lastSavedSong;
    }

    public List<MonthlyCountDTO> getMonthCountList() {
        return monthCountList;
    }

    public void setMonthCountList(List<MonthlyCountDTO> monthCountList) {
        this.monthCountList = monthCountList;
    }
}
