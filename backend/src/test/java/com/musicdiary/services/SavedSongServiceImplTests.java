package com.musicdiary.services;

import com.musicdiary.dtos.EditNoteRequestDTO;
import com.musicdiary.dtos.SaveSongRequestDTO;
import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.dtos.SavedSongFilterRequestDTO;
import com.musicdiary.exceptions.SavedSongNotFoundException;
import com.musicdiary.exceptions.SongAlreadySavedException;
import com.musicdiary.exceptions.UserNotFoundException;
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
public class SavedSongServiceImplTests {

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
        when(savedSongRepository.findByUserOrderByCreatedAtDesc(user)).thenReturn(savedSongList);

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
        when(savedSongRepository.findByUserOrderByCreatedAtDesc(user)).thenReturn(savedSongList);

        List<SaveSongResponseDTO> savedSongListDTO = savedSongServiceImpl.listSavedSongs();

        assertNotNull(savedSongListDTO);
        assertEquals(1, savedSongListDTO.size());
    }

    @Test
    void listByArtist_shouldReturnListOfSavedSongResponseDTO() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        Song song = new Song();
        song.setId(1L);
        song.setTitle("test");
        song.setArtistName("testName");

        SavedSong savedSong = new SavedSong();
        savedSong.setId(1L);
        savedSong.setUser(user);
        savedSong.setSong(song);

        List<SavedSong> savedSongList = new ArrayList<>();
        savedSongList.add(savedSong);

        when(savedSongRepository.findByUserAndSongArtistName(user, "testName")).thenReturn(savedSongList);

        List<SaveSongResponseDTO> result = savedSongServiceImpl.listByArtist("testName");

        assertEquals(1, result.size());
        assertEquals("testName",result.get(0).getArtistName());
        assertEquals("test",result.get(0).getTitle());
        verify(savedSongRepository).findByUserAndSongArtistName(user, "testName");
    }

    @Test
    void getSavedSongs_shouldThrowExceptionIfSaveSongFilterRequestDTOIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> savedSongServiceImpl.getSavedSongs(null));

        assertEquals("saveSongFilterRequestDTO must be valid", exception.getMessage());
    }

    @Test
    void getSavedSongs_shouldThrowExceptionIfUserNotFound() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        SavedSongFilterRequestDTO savedSongFilterRequestDTO = new SavedSongFilterRequestDTO();

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> savedSongServiceImpl.getSavedSongs(savedSongFilterRequestDTO));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@email.com");
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

    @Test
    void saveSong_shouldThrowExceptionIfSaveSongRequestDTOIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> savedSongServiceImpl.saveSong(null));

        assertEquals("SaveSongRequest must be valid", exception.getMessage());
    }

    @Test
    void saveSong_shouldThrowExceptionIfUserNotFound() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        SaveSongRequestDTO saveSongRequestDTO = new SaveSongRequestDTO();

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> savedSongServiceImpl.saveSong(saveSongRequestDTO));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@email.com");
    }

    @Test
    void saveSong_shouldCreateNewSong_whenSongNotFoundInDatabase () {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setEmail("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        SaveSongRequestDTO saveSongRequestDTO = new SaveSongRequestDTO();
        saveSongRequestDTO.setTitle("test");
        saveSongRequestDTO.setArtistName("testArtist");

        when(songRepository.findByTitleAndArtistName("test", "testArtist"))
                .thenReturn(Optional.empty());

        Song song = new Song();
        song.setTitle("test");
        song.setArtistName("testArtist");

        SavedSong savedSong = new SavedSong();
        savedSong.setUser(user);
        savedSong.setSong(song);

        when(savedSongRepository.save(any(SavedSong.class))).thenReturn(savedSong);


        SaveSongResponseDTO songResponseDTO = savedSongServiceImpl.saveSong(saveSongRequestDTO);

        assertNotNull(songResponseDTO);
        assertEquals("test", songResponseDTO.getTitle());
        assertEquals("testArtist", songResponseDTO.getArtistName());
    }

    @Test
    void saveSong_shouldThrowExceptionIfSongAlreadySavedByUser() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setFirstName("test");
        user.setEmail("test@email.com");

        SaveSongRequestDTO saveSongRequestDTO = new SaveSongRequestDTO();
        saveSongRequestDTO.setArtistName("test");
        saveSongRequestDTO.setTitle("testTitle");

        Song song = new Song();
        song.setArtistName("test");
        song.setTitle("testTitle");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        when(songRepository.findByTitleAndArtistName("testTitle", "test")).thenReturn(Optional.of(song));
        when(savedSongRepository.existsByUserAndSong(user, song)).thenReturn(true);

        SongAlreadySavedException exception = assertThrows(
                SongAlreadySavedException.class,
                () -> savedSongServiceImpl.saveSong(saveSongRequestDTO));

        assertEquals("This song is already saved", exception.getMessage());
        verify(savedSongRepository).existsByUserAndSong(user, song);
    }

    @Test
    void deleteSong_shouldDeleteSong_withValidId() {

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

        SavedSong savedSong = new SavedSong();
        savedSong.setId(1L);
        savedSong.setUser(user);
        savedSong.setSong(song);

        when(savedSongRepository.findById(1L)).thenReturn(Optional.of(savedSong));

        savedSongServiceImpl.deleteSong(1L);

        verify(savedSongRepository, times(1)).delete(savedSong);
    }

    @Test
    void deleteSong_shouldThrowException_withInvalidId () {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@email.com");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setEmail("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        when(savedSongRepository.findById(1L)).thenReturn(Optional.empty());

        SavedSongNotFoundException exception = assertThrows(
                SavedSongNotFoundException.class,
                () -> savedSongServiceImpl.deleteSong(1L));

        assertEquals("Saved song not found", exception.getMessage());

        verify(savedSongRepository).findById(1L);
    }

    @Test
    void editNoteWithValidData_shouldReturnSaveSongResponseDTO() {

        Song song = new Song();
        song.setTitle("Test");
        song.setArtistName("TestArtist");

        SavedSong savedSong = new SavedSong();
        savedSong.setId(1L);
        savedSong.setSong(song);

        EditNoteRequestDTO editNoteRequestDTO = new EditNoteRequestDTO();
        editNoteRequestDTO.setNote("TestNote");

        when(savedSongRepository.findById(1L)).thenReturn(Optional.of(savedSong));

        SaveSongResponseDTO songResponseDTO = savedSongServiceImpl.editNote(1L, editNoteRequestDTO);

        assertNotNull(songResponseDTO);
        assertEquals("TestNote", songResponseDTO.getNote());

    }

    @Test
    void editNoteWithInvalidId_shouldThrowException() {

        EditNoteRequestDTO editNoteRequestDTO = new EditNoteRequestDTO();
        editNoteRequestDTO.setNote("TestNote");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> savedSongServiceImpl.editNote(-2L, editNoteRequestDTO));

        assertEquals("Id must be positive", exception.getMessage());
    }

    @Test
    void editNoteWithNonExistentSong_shouldThrowException() {

        EditNoteRequestDTO editNoteRequestDTO = new EditNoteRequestDTO();

        when(savedSongRepository.findById(1L)).thenReturn(Optional.empty());

        SavedSongNotFoundException exception = assertThrows(
                SavedSongNotFoundException.class,
                () -> savedSongServiceImpl.editNote(1L, editNoteRequestDTO));


        assertEquals("Saved song not found", exception.getMessage());
    }
}
