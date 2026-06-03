package com.musicdiary.repositories;

import com.musicdiary.models.SavedSong;
import com.musicdiary.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedSongRepository extends JpaRepository<SavedSong, Long> {

    List<SavedSong> findByUserOrderByCreatedAtDesc(User user);
}
