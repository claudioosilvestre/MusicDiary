package com.musicdiary.services;

import com.musicdiary.converters.SavedSongConverter;
import com.musicdiary.dtos.EditNoteRequestDTO;
import com.musicdiary.dtos.SaveSongRequestDTO;
import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.exceptions.InvalidUserException;
import com.musicdiary.exceptions.SavedSongNotFoundException;
import com.musicdiary.models.SavedSong;
import com.musicdiary.models.Song;
import com.musicdiary.models.User;
import com.musicdiary.repositories.SavedSongRepository;
import com.musicdiary.repositories.SongRepository;
import com.musicdiary.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedSongServiceImpl implements SavedSongService {

    private UserRepository userRepository;
    private SongRepository songRepository;
    private SavedSongRepository savedSongRepository;
    private SavedSongConverter savedSongConverter;

    public SavedSongServiceImpl(UserRepository userRepository, SongRepository songRepository, SavedSongRepository savedSongRepository, SavedSongConverter savedSongConverter) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.savedSongRepository = savedSongRepository;
        this.savedSongConverter = savedSongConverter;
    }

    @Override
    public List<SaveSongResponseDTO> listSavedSongs() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<SavedSong> savedSongList = savedSongRepository.findByUserOrderByCreatedAtDesc(user);

        List<SaveSongResponseDTO> saveSongResponse = savedSongList.stream()
                .map(song -> SavedSongConverter.toResponseDTO(song))
                .collect(Collectors.toList());

        return saveSongResponse;
    }

    @Override
    public List<SaveSongResponseDTO> listByArtist(String artistName) {

        if(artistName == null || artistName.isBlank()) {
            throw new IllegalArgumentException("Artist name must be valid");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<SavedSong> savedSongList = savedSongRepository.findByUserAndSongArtistName(user, artistName);

        List<SaveSongResponseDTO> responses = savedSongList.stream()
                .map(song -> SavedSongConverter.toResponseDTO(song))
                .collect(Collectors.toList());

        return responses;
    }

    @Override
    public SaveSongResponseDTO saveSong(SaveSongRequestDTO saveSongRequestDTO) {

        if(saveSongRequestDTO == null) {
            throw new IllegalArgumentException("SaveSongRequest must be valid");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Song song = songRepository.findByTitleAndArtistName(saveSongRequestDTO.getTitle(), saveSongRequestDTO.getArtistName())
                .orElseGet(() -> createSong(saveSongRequestDTO));

        SavedSong savedSong = new SavedSong();
        savedSong.setUser(user);
        savedSong.setSong(song);
        savedSong.setNote(saveSongRequestDTO.getNote());

        savedSongRepository.save(savedSong);

        return SavedSongConverter.toResponseDTO(savedSong);

    }

    @Override
    public SaveSongResponseDTO editNote(Long id, EditNoteRequestDTO editNoteRequestDTO) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }

        if(editNoteRequestDTO == null) {
            throw new IllegalArgumentException("EditNoteRequest must be valid");
        }

        SavedSong savedSong = savedSongRepository.findById(id)
                .orElseThrow(() -> new SavedSongNotFoundException());

        savedSong.setNote(editNoteRequestDTO.getNote());

        savedSongRepository.save(savedSong);

        return savedSongConverter.toResponseDTO(savedSong);
    }

    @Override
    public void deleteSong(Long id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        SavedSong savedSong = savedSongRepository.findById(id)
                .orElseThrow(()-> new SavedSongNotFoundException());

        if(!user.equals(savedSong.getUser())) {
            throw new InvalidUserException();
        }
        savedSongRepository.delete(savedSong);
    }

    public Song createSong(SaveSongRequestDTO saveSongRequestDTO) {
        Song song = new Song();

        song.setArtistName(saveSongRequestDTO.getArtistName());
        song.setTitle(saveSongRequestDTO.getTitle());
        song.setImageUrl(saveSongRequestDTO.getImageUrl());
        song.setLastFmUrl(saveSongRequestDTO.getLastFmUrl());

        songRepository.save(song);

        return song;
    }
}
