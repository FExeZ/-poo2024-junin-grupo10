package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Repository.SongRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.SongService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongServiceImp implements SongService {

    @Autowired
    SongRepository songRepository;

    @Override
    public void createSong(Song song) {
        songRepository.save(song);
    }

    @Override
    public List<Song> getAll() {
        return songRepository.findAll();
    }
}