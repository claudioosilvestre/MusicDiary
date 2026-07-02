package com.musicdiary.repositories;

import com.musicdiary.models.SavedSong;
import com.musicdiary.models.Song;
import com.musicdiary.models.User;
import com.musicdiary.projections.ArtistCount;
import com.musicdiary.projections.MonthlyCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SavedSongRepository extends JpaRepository<SavedSong, Long> {

    Boolean existsByUserAndSong(User user, Song song);

    List<SavedSong> findByUserOrderByCreatedAtDesc(User user);

    List<SavedSong> findByUserAndSongArtistName(User user, String artistName);

    List<SavedSong> findByUserAndSongTitle(User user, String songTitle);

    List<SavedSong> findByUserAndCreatedAtBetween(User user, LocalDate from, LocalDate to);

    Long countByUser(User user);

    @Query("SELECT ss.song.artistName, COUNT(ss) FROM SavedSong ss WHERE ss.user = :user GROUP BY ss.song.artistName ORDER BY COUNT(ss) DESC")
    List<ArtistCount> findArtistCountsByUser(@Param("user") User user);

    SavedSong findFirstByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT YEAR(ss.createdAt), MONTH(ss.createdAt), COUNT(ss) FROM SavedSong ss WHERE ss.user = :user GROUP BY YEAR(ss.createdAt), MONTH(ss.createdAt) ORDER BY YEAR(ss.createdAt) ASC, MONTH(ss.createdAt) ASC")
    List<MonthlyCountProjection> findMonthlyCountsByUser(@Param("user") User user);

}
