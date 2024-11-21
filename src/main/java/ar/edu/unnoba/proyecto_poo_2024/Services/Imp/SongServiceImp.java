package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
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
    public void createSong(User user, Song song) {
        // Verificamos si el usuario tiene permisos para crear la canción
        if (!user.canCreateSong()) {
            // Si no tiene permisos, lanzamos una excepción
            throw new UnsupportedOperationException("Este usuario no tiene permisos para crear canciones.");
        }

        // Si el usuario tiene permisos, guardamos la canción
        songRepository.save(song);
    }


    @Override
    public List<Song> getAll() {
        return songRepository.findAll();
    }
}