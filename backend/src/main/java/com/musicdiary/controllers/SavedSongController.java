package com.musicdiary.controllers;

import com.musicdiary.dtos.SaveSongRequestDTO;
import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.services.SavedSongService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saved-songs")
public class SavedSongController {

    private SavedSongService savedSongService;

    public SavedSongController(SavedSongService savedSongService) {
        this.savedSongService = savedSongService;
    }

    @GetMapping
    public ResponseEntity<List<SaveSongResponseDTO>> listSavedSongs() {

        return ResponseEntity.ok(savedSongService.listSavedSongs());
    }


    @PostMapping
    public ResponseEntity<SaveSongResponseDTO> saveSong(@Valid @RequestBody SaveSongRequestDTO saveSongRequestDTO) {

        SaveSongResponseDTO saveSongResponseDTO = savedSongService.saveSong(saveSongRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveSongResponseDTO);

    }
}
