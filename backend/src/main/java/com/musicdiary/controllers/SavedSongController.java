package com.musicdiary.controllers;

import com.musicdiary.dtos.EditNoteRequestDTO;
import com.musicdiary.dtos.SaveSongRequestDTO;
import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.dtos.SavedSongFilterRequestDTO;
import com.musicdiary.services.SavedSongService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/artist/{artistName}")
    public ResponseEntity <List<SaveSongResponseDTO>>  listSavedSongByArtistName (@PathVariable String artistName) {

        return ResponseEntity.ok(savedSongService.listByArtist(artistName));
    }

    @GetMapping
    public ResponseEntity<List<SaveSongResponseDTO>> getSavedSongs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artistName,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
            ) {

        SavedSongFilterRequestDTO savedSongFilterRequestDTO = new SavedSongFilterRequestDTO();
        savedSongFilterRequestDTO.setTitle(title);
        savedSongFilterRequestDTO.setArtistName(artistName);
        savedSongFilterRequestDTO.setFrom(from);
        savedSongFilterRequestDTO.setTo(to);

        return ResponseEntity.ok(savedSongService.getSavedSongs(savedSongFilterRequestDTO));
    }

    @PostMapping
    public ResponseEntity<SaveSongResponseDTO> saveSong(@Valid @RequestBody SaveSongRequestDTO saveSongRequestDTO) {

        SaveSongResponseDTO saveSongResponseDTO = savedSongService.saveSong(saveSongRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveSongResponseDTO);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<SaveSongResponseDTO> editNote(@PathVariable Long id, @Valid @RequestBody EditNoteRequestDTO editNoteRequestDTO) {

        SaveSongResponseDTO saveSongResponseDTO = savedSongService.editNote(id, editNoteRequestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(saveSongResponseDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {

        savedSongService.deleteSong(id);

        return ResponseEntity.noContent().build();
    }
}
