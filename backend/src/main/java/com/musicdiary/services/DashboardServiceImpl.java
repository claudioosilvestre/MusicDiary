package com.musicdiary.services;

import com.musicdiary.converters.SavedSongConverter;
import com.musicdiary.dtos.DashboardResponseDTO;
import com.musicdiary.dtos.MonthlyCountDTO;
import com.musicdiary.dtos.SaveSongResponseDTO;
import com.musicdiary.exceptions.UserNotFoundException;
import com.musicdiary.models.SavedSong;
import com.musicdiary.models.User;
import com.musicdiary.projections.ArtistCount;
import com.musicdiary.projections.MonthlyCountProjection;
import com.musicdiary.repositories.SavedSongRepository;
import com.musicdiary.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private SavedSongRepository savedSongRepository;
    private UserRepository userRepository;

    public DashboardServiceImpl(SavedSongRepository savedSongRepository, UserRepository userRepository) {
        this.savedSongRepository = savedSongRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DashboardResponseDTO getDashboard(String email) {

        if(email == null) {
            throw new IllegalArgumentException("Email must be valid");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        DashboardResponseDTO dashboardResponseDTO = new DashboardResponseDTO();
        dashboardResponseDTO.setTotalMusicsSaved(totalMusicSavedByUser(user));

        ArtistCount artistCount = mostSavedArtistByUser(user);
        if(artistCount == null) {
            dashboardResponseDTO.setMostSavedArtist(null);
            dashboardResponseDTO.setTimesOfMostSavedArtist(0L);
        } else {
            dashboardResponseDTO.setMostSavedArtist(artistCount.getArtistName());
            dashboardResponseDTO.setTimesOfMostSavedArtist(artistCount.getCount());
        }

        dashboardResponseDTO.setLastSavedSong(lastSavedSongByUser(user));

        List<MonthlyCountProjection> monthlyCountList = monthCountListByUser(user);

        List<MonthlyCountDTO> countDTOList = monthlyCountList.stream()
                .map(monthlyCount -> toMonthlyCountDTO(monthlyCount))
                .toList();

        dashboardResponseDTO.setMonthCountList(countDTOList);

        return dashboardResponseDTO;
    }

    private Long totalMusicSavedByUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User must be valid");
        }

        return savedSongRepository.countByUser(user);
    }

    private ArtistCount mostSavedArtistByUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User must be valid");
        }

        List<ArtistCount> artistCountsList = savedSongRepository.findArtistCountsByUser(user);

        if(artistCountsList.isEmpty()) {
            return null;
        }

        return artistCountsList.getFirst();
    }

    private SaveSongResponseDTO lastSavedSongByUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User must be valid");
        }

        SavedSong savedSong = savedSongRepository.findFirstByUserOrderByCreatedAtDesc(user);

        if (savedSong == null) {
            return null;
        }

        SaveSongResponseDTO saveSongResponseDTO = SavedSongConverter.toResponseDTO(savedSong);

        return saveSongResponseDTO;

    }

    private List<MonthlyCountProjection> monthCountListByUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User must be valid");
        }

        return savedSongRepository.findMonthlyCountsByUser(user);
    }

    private MonthlyCountDTO toMonthlyCountDTO(MonthlyCountProjection monthlyCountProjection) {
        
        MonthlyCountDTO monthlyCountDTO = new MonthlyCountDTO();
        monthlyCountDTO.setCount(monthlyCountProjection.getCount());

        Integer year = monthlyCountProjection.getYear();
        Integer month = monthlyCountProjection.getMonth();
        String monthAndYear = String.format("%d-%02d", year, month);


        monthlyCountDTO.setMonth(monthAndYear);

        return monthlyCountDTO;
    }
}
