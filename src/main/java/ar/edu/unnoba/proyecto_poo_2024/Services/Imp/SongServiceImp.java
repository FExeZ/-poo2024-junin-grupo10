package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Repository.PlaylistRepository;
import ar.edu.unnoba.proyecto_poo_2024.Repository.SongRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.SongService;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongServiceImp implements SongService {

    @Autowired
    SongRepository songRepository;

    @Autowired
    PlaylistRepository playlistRepository;

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
    public void deleteSongByIdAndUser(Long songId, Long userId) throws Exception {
        // Buscar la canción
        Optional<Song> songOptional = songRepository.findById(songId);
        if (!songOptional.isPresent()) {
            throw new NoSuchElementException("La canción no existe.");
        }

        Song song = songOptional.get();

        // Verificar que el usuario sea el creador de la canción
        if (song.getMusicArtistUser() == null || !song.getMusicArtistUser().getId().equals(userId)) {
            throw new IllegalAccessException("No tienes permiso para eliminar esta canción.");
        }

        // Eliminar la canción y sus asociaciones automáticamente (gracias a CascadeType.REMOVE)
        songRepository.delete(song);
    }


    @Override
    public List<Song> getAll() {
        return songRepository.findAll();
    }
}