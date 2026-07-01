package com.musicdiary.services;

import com.musicdiary.dtos.DashboardResponseDTO;
import com.musicdiary.models.SavedSong;
import com.musicdiary.models.Song;
import com.musicdiary.models.User;
import com.musicdiary.projections.ArtistCount;
import com.musicdiary.projections.MonthlyCountProjection;
import com.musicdiary.repositories.SavedSongRepository;
import com.musicdiary.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImplTests {

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Mock
    private SavedSongRepository savedSongRepository;

    @Mock
    private UserRepository userRepository;


    @Test
    void getDashboard_shouldReturnDashboardResponseDTO () {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setId(1L);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(savedSongRepository.countByUser(user)).thenReturn(10L);

        ArtistCount artistCount = new ArtistCount() {
            @Override
            public String getArtistName() {
                return "test";
            }

            @Override
            public Long getCount() {
                return 5L;
            }
        };

        List<ArtistCount> artistCountList = new ArrayList<>();
        artistCountList.add(artistCount);

        when(savedSongRepository.findArtistCountsByUser(user)).thenReturn(artistCountList);

        Song song = new Song();
        song.setId(1L);
        song.setTitle("test");
        song.setArtistName("testName");

        SavedSong savedSong = new SavedSong();
        savedSong.setId(1L);
        savedSong.setUser(user);
        savedSong.setSong(song);
        savedSong.setNote("testNote");

        when(savedSongRepository.findFirstByUserOrderByCreatedAtDesc(user)).thenReturn(savedSong);

        MonthlyCountProjection monthlyCountProjection = new MonthlyCountProjection() {
            @Override
            public Integer getYear() {
                return 2026;
            }

            @Override
            public Integer getMonth() {
                return 10;
            }

            @Override
            public Long getCount() {
                return 5L;
            }
        };

        List<MonthlyCountProjection> monthlyCountProjectionList = new ArrayList<>();
        monthlyCountProjectionList.add(monthlyCountProjection);
        when(savedSongRepository.findMonthlyCountsByUser(user)).thenReturn(monthlyCountProjectionList);

        DashboardResponseDTO result = dashboardService.getDashboard("test@mail.com");

        assertNotNull(result);
        assertEquals(10L, result.getTotalMusicsSaved());
        assertEquals(monthlyCountProjectionList.get(0).getCount(), result.getMonthCountList().get(0).getCount());
        assertEquals(1, result.getMonthCountList().size());
        assertEquals("2026-10", result.getMonthCountList().get(0).getMonth());
        assertEquals(artistCount.getArtistName(), result.getMostSavedArtist());
        assertEquals(artistCount.getCount(), result.getTimesOfMostSavedArtist());
        assertEquals(savedSong.getSong().getArtistName(), result.getLastSavedSong().getArtistName());
        assertEquals(savedSong.getSong().getTitle(), result.getLastSavedSong().getTitle());
        assertEquals(savedSong.getSong().getId(), result.getLastSavedSong().getId());
        assertEquals(savedSong.getNote(), result.getLastSavedSong().getNote());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(savedSongRepository, times(1)).countByUser(user);
        verify(savedSongRepository, times(1)).findArtistCountsByUser(user);
        verify(savedSongRepository, times(1)).findMonthlyCountsByUser(user);
        verify(savedSongRepository, times(1)).findFirstByUserOrderByCreatedAtDesc(user);
    }

}
