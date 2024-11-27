package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Model.Song;

import java.util.List;

import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import org.springframework.stereotype.Service;

@Service
public interface SongService {
    void createSong(User user,Song song);
    boolean deleteSongByIdAndUser(Long songId, Long userId);
    List<Song> getAll();
}
