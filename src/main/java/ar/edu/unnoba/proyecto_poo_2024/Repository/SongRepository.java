package ar.edu.unnoba.proyecto_poo_2024.Repository;

import ar.edu.unnoba.proyecto_poo_2024.Model.Song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findAllByMusicArtistUserId(Long userId);

}
