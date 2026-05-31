package com.musicdiary.services;

import com.musicdiary.converters.SavedSongConverter;
import com.musicdiary.dtos.SaveSongRequestDTO;
import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.models.SavedSong;
import com.musicdiary.models.Song;
import com.musicdiary.models.User;
import com.musicdiary.repositories.SavedSongRepository;
import com.musicdiary.repositories.SongRepository;
import com.musicdiary.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SavedSongServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SavedSongRepository savedSongRepository;
    @Mock
    private SongRepository songRepository;
    @InjectMocks
    private SavedSongServiceImpl savedSongServiceImpl;

    @Test
    void listSavedSongs_shouldReturnListOfSavedSongs() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setEmail("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        List<SavedSong> savedSongList = new ArrayList<>();
        when(savedSongRepository.findByUser(user)).thenReturn(savedSongList);

        List<SaveSongResponseDTO> saveSongResponse = savedSongServiceImpl.listSavedSongs();

        assertNotNull(saveSongResponse);
        assertEquals(0, saveSongResponse.size());

    }

    @Test
    void listSavedSongs_shouldThrowException_whenUserNotFound() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> savedSongServiceImpl.listSavedSongs());

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByEmail("test@email.com");
    }

    @Test
    void listSavedSongs_shouldReturnListWithSongs_whenUserHasSavedSongs() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setEmail("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        List<SavedSong> savedSongList = new ArrayList<>();

        Song song = new Song();
        song.setTitle("Test");
        song.setArtistName("TestArtist");

        SavedSong savedSong = new SavedSong();
        savedSong.setSong(song);

        savedSongList.add(savedSong);
        when(savedSongRepository.findByUser(user)).thenReturn(savedSongList);

        List<SaveSongResponseDTO> savedSongListDTO = savedSongServiceImpl.listSavedSongs();

        assertNotNull(savedSongListDTO);
        assertEquals(1, savedSongListDTO.size());
    }

    @Test
    void saveSong_withValidData_shouldReturnSavedSong() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setEmail("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        Song song = new Song();
        song.setTitle("Test");
        song.setArtistName("TestArtist");
        when(songRepository.findByTitleAndArtistName("Test", "TestArtist")).thenReturn(Optional.of(song));

        SavedSong savedSong= new SavedSong();
        savedSong.setUser(user);
        savedSong.setSong(song);

        when(savedSongRepository.save(any(SavedSong.class))).thenReturn(savedSong);

        SaveSongRequestDTO saveSongRequestDTO = new SaveSongRequestDTO();
        saveSongRequestDTO.setArtistName("TestArtist");
        saveSongRequestDTO.setTitle("Test");

        SaveSongResponseDTO songResponseDTO = savedSongServiceImpl.saveSong(saveSongRequestDTO);

        assertNotNull(songResponseDTO);
        assertEquals("Test", songResponseDTO.getTitle());
        assertEquals("TestArtist", songResponseDTO.getArtistName());
    }
}
