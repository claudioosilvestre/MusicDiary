package com.musicdiary.controllers;

import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.services.JwtService;
import com.musicdiary.services.SavedSongService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SavedSongController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SavedSongControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SavedSongService savedSongService;

    @MockBean
    private JwtService jwtService;

    @Test
    void shouldReturnSavedSongsList() throws Exception{

        SaveSongResponseDTO saveSongResponseDTO = new SaveSongResponseDTO();
        saveSongResponseDTO.setId(1L);
        saveSongResponseDTO.setTitle("test");
        saveSongResponseDTO.setArtistName("testName");
        saveSongResponseDTO.setNote("testNote");

        when(savedSongService.listSavedSongs()).thenReturn(List.of(saveSongResponseDTO));

        mockMvc.perform(get("/saved-songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("test"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].artistName").value("testName"))
                .andExpect(jsonPath("$[0].note").value("testNote"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(savedSongService).listSavedSongs();
    }

    @Test
    void shouldReturnEmptySaveSongList() throws Exception {

        when(savedSongService.listSavedSongs()).thenReturn(List.of());

        mockMvc.perform(get("/saved-songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(savedSongService).listSavedSongs();
    }

    @Test
    void shouldReturnSavedSongsByArtist() throws Exception {

        SaveSongResponseDTO saveSongResponseDTO = new SaveSongResponseDTO();
        saveSongResponseDTO.setId(1L);
        saveSongResponseDTO.setTitle("Sandman");
        saveSongResponseDTO.setArtistName("Metallica");
        saveSongResponseDTO.setNote("test");

        when(savedSongService.listByArtist("Metallica")).thenReturn(List.of(saveSongResponseDTO));

        mockMvc.perform(get("/saved-songs/artist/Metallica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sandman"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].artistName").value("Metallica"))
                .andExpect(jsonPath("$[0].note").value("test"))
                .andExpect(jsonPath("$.size()").value(1));

        verify(savedSongService).listByArtist("Metallica");
    }

    @Test
    void listByArtistShouldReturnEmptySavedSongList() throws Exception {

        when(savedSongService.listByArtist("Metallica")).thenReturn(List.of());

        mockMvc.perform(get("/saved-songs/artist/Metallica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(savedSongService).listByArtist("Metallica");
    }
}
