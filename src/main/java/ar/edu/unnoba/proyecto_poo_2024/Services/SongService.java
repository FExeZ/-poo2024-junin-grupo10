package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.UpdateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;

import java.util.List;

import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;

import org.springframework.stereotype.Service;

@Service
public interface SongService {

    Song findById(Long songId);

    void createSong(User user, CreateSongRequestDTO song);

    void deleteSongByIdAndUser(Long songId, Long userId) throws Exception;

    public void updateSong(Long userId, Long songId, UpdateSongRequestDTO song) throws Exception;

    List<Song> getAll();

    List<Song> getCreatedSongsByUser(Long userId);

    Song getSongById(Long songId);

    List<Song> getFilteredSongs(String artist, Genre genre);

}