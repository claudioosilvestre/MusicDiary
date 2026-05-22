package com.musicdiary.controllers;

import com.musicdiary.dtos.ArtistDTO;
import com.musicdiary.dtos.TrackDTO;
import com.musicdiary.services.LastFmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private LastFmService lastFmService;

    public SearchController(LastFmService lastFmService) {
        this.lastFmService = lastFmService;
    }

    @GetMapping("/artists")
    public ResponseEntity<List<ArtistDTO>> listArtists(@RequestParam String name) {

        List<ArtistDTO> artistDTOS = lastFmService.searchArtists(name);

        return ResponseEntity.ok(artistDTOS);
    }

    @GetMapping("/tracks")
    public ResponseEntity<List<TrackDTO>> listTracks(@RequestParam String name) {

        List<TrackDTO> trackDTOS = lastFmService.searchTracks(name);

        return ResponseEntity.ok(trackDTOS);
    }
}
