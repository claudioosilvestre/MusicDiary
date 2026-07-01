package com.musicdiary.services;

import com.musicdiary.dtos.EditNoteRequestDTO;
import com.musicdiary.dtos.SaveSongRequestDTO;
import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.dtos.SavedSongFilterRequestDTO;

import java.util.List;

public interface SavedSongService {

    List<SaveSongResponseDTO> listSavedSongs();

    List<SaveSongResponseDTO> listByArtist(String artistName);

    List<SaveSongResponseDTO> getSavedSongs(SavedSongFilterRequestDTO savedSongFilterRequestDTO);

    SaveSongResponseDTO saveSong(SaveSongRequestDTO saveSongRequestDTO);

    SaveSongResponseDTO editNote(Long id, EditNoteRequestDTO editNoteRequestDTO);

    void deleteSong(Long id);

}
