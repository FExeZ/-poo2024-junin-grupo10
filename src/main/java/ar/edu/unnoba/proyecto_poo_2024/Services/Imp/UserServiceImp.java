package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Repository.PlaylistRepository;
import ar.edu.unnoba.proyecto_poo_2024.Repository.SongRepository;
import ar.edu.unnoba.proyecto_poo_2024.Repository.UserRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.PlaylistService;
import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;
import ar.edu.unnoba.proyecto_poo_2024.Util.JwtTokenUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    SongRepository songRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void canCreateSong(User user, Song song) {
        if (user.canCreateSong()) { // Uso del polimorfismo en la verificación
            songRepository.save(song);
        } else {
            throw new UnsupportedOperationException("Este usuario no tiene permisos para crear canciones.");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            userRepository.delete(user.get()); // Eliminar el usuario si está presente
        } else {
            throw new NoSuchElementException("Usuario no encontrado");
        }
    }

    @Override
    public void createUserPlaylist(Long userId, Playlist playlist) {
        // Buscar el usuario por su ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        // Establecer la relación entre la playlist y el usuario
        playlist.setUser(user);

        // Crear la playlist usando el servicio de playlists
        playlistService.createPlaylist(playlist);
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        return user;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));  // Maneja el caso de no encontrar al usuario
    }

    public void addSongToPlaylist(Long userId, Long playlistId, Song song) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Usuario no encontrado"));

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new Exception("Playlist no encontrada"));

        Song songToAdd = songRepository.findById(song.getId())
                .orElseThrow(() -> new Exception("Canción no encontrada"));

        if (playlist.getSongs().contains(songToAdd)) {
            throw new Exception("La canción ya está en la playlist.");
        }

        playlist.getSongs().add(songToAdd);
        playlistRepository.save(playlist);
    }
}
