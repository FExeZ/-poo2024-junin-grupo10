package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Dto.SongResponseDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;

import java.util.List;

import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import org.springframework.stereotype.Service;

@Service
public interface SongService {


    Song findById(Long songId);

    void createSong(User user, Song song);

    void deleteSongByIdAndUser(Long songId, Long userId) throws Exception;

    public void updateSong(Song song) throws Exception;

    List<Song> getAll();

    List<Song> getCreatedSongsByUser(Long userId);

    SongResponseDTO getSongById(Long songId);

}