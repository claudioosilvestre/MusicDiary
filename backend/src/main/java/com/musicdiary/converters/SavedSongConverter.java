package com.musicdiary.converters;

import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.models.SavedSong;
import org.springframework.stereotype.Component;

@Component
public class SavedSongConverter {


    public static SaveSongResponseDTO toResponseDTO(SavedSong savedSong) {

        SaveSongResponseDTO saveSongResponseDTO = new SaveSongResponseDTO();
        saveSongResponseDTO.setId(savedSong.getId());
        saveSongResponseDTO.setTitle(savedSong.getSong().getTitle());
        saveSongResponseDTO.setArtistName(savedSong.getSong().getArtistName());
        saveSongResponseDTO.setImageUrl(savedSong.getSong().getImageUrl());
        saveSongResponseDTO.setLastFmUrl(savedSong.getSong().getLastFmUrl());
        saveSongResponseDTO.setCreatedAt(savedSong.getCreatedAt());
        saveSongResponseDTO.setNote(savedSong.getNote());

        return saveSongResponseDTO;
    }
}
