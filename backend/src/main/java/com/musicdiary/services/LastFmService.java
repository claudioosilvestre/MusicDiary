package com.musicdiary.services;

import com.musicdiary.dtos.ArtistDTO;
import com.musicdiary.dtos.TrackDTO;

import java.util.List;

public interface LastFmService {

    List<ArtistDTO> searchArtists(String artistName);
    
    List<TrackDTO> searchTracks(String trackName);
}
