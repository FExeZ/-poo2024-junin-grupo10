package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Model.Song;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface SongService {
    void createSong(Long id, Song song);

    List<Song> getAll();
}
