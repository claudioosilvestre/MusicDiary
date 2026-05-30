package com.musicdiary.services;

import com.musicdiary.dtos.SaveSongRequestDTO;
import com.musicdiary.dtos.SaveSongResponseDTO;

import java.util.List;

public interface SavedSongService {

    List<SaveSongResponseDTO> listSavedSongs();

    SaveSongResponseDTO saveSong(SaveSongRequestDTO saveSongRequestDTO);

    void deleteSong(Long id);

}
