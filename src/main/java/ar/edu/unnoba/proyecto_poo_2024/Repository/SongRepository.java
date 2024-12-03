package ar.edu.unnoba.proyecto_poo_2024.Repository;

import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findAllByMusicArtistUserId(Long userId);

    List<Song> findByMusicArtistUser_ArtisticNameAndGenre(String artist, Genre genre);

    List<Song> findByMusicArtistUser_ArtisticName(String artist);

    List<Song> findByGenre(Genre genre);

}
