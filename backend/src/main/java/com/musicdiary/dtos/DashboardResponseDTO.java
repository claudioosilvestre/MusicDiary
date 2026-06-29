package com.musicdiary.dtos;

import java.util.List;

public class DashboardResponseDTO {

    private Long totalMusicsSaved;

    private String mostSavedArtist;

    private Long timesOfMostSavedArtist;

    private SaveSongResponseDTO lastSavedSong;

    private List<MonthlyCountDTO> monthCountList;

    public Long getTotalMusicsSaved() {
        return totalMusicsSaved;
    }

    public void setTotalMusicsSaved(Long totalMusicsSaved) {
        this.totalMusicsSaved = totalMusicsSaved;
    }

    public String getMostSavedArtist() {
        return mostSavedArtist;
    }

    public void setMostSavedArtist(String mostSavedArtist) {
        this.mostSavedArtist = mostSavedArtist;
    }

    public Long getTimesOfMostSavedArtist() {
        return timesOfMostSavedArtist;
    }

    public void setTimesOfMostSavedArtist(Long timesOfMostSavedArtist) {
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
