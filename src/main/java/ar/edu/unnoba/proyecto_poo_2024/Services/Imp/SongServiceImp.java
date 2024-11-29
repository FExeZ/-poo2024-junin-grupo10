package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Dto.SongResponseDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.*;
import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import ar.edu.unnoba.proyecto_poo_2024.Repository.PlaylistRepository;
import ar.edu.unnoba.proyecto_poo_2024.Repository.SongRepository;
import ar.edu.unnoba.proyecto_poo_2024.Repository.UserRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.SongService;
import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongServiceImp implements SongService {

    @Autowired
    SongRepository songRepository;

    @Autowired
    UserRepository userRepository;

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

        // Eliminar la canción y sus asociaciones automáticamente (gracias a
        // CascadeType.REMOVE)
        songRepository.delete(song);
    }

    @Override
    public void updateSong(Song song) throws Exception {
        Song songDB = songRepository.findById(song.getId())
                .orElseThrow(() -> new Exception("Cancion no encontrada"));
        songDB.setName(song.getName());
        songDB.setGenre(song.getGenre());
        songRepository.save(songDB);
    }

    @Override
    public List<Song> getAll() {
        return songRepository.findAll();
    }

    public List<Song> getCreatedSongsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("El usuario no existe."));

        if (!user.canCreateSong()) {
            throw new IllegalArgumentException("El usuario no tiene permisos para crear canciones.");
        }

        // Dado que sabemos que es un artista, podemos acceder a las canciones creadas
        return songRepository.findAllByMusicArtistUserId(userId);
    }

    public Song findById(Long SongId) {
        return songRepository.findById(SongId)
                .orElseThrow(() -> new RuntimeException("Cancion no encontrada")); // Maneja el caso de no encontrar al
                                                                                   // usuario
    }

    public SongResponseDTO getSongById(Long songId) {
        return songRepository.findById(songId)
                .map(song -> new SongResponseDTO(
                        song.getId(),
                        song.getName(),
                        song.getGenre()))
                .orElse(null); // Retorna null si no se encuentra la canción
    }
}
