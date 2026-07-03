package com.musicdiary.repositories;

import com.musicdiary.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Optional<Song> findByTitleAndArtistName(String title, String artistName);
}
