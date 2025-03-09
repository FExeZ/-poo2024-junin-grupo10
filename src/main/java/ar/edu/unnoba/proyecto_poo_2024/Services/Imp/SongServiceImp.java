package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.UpdateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Repository.SongRepository;
import ar.edu.unnoba.proyecto_poo_2024.Repository.UserRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.SongService;

@Service
public class SongServiceImp implements SongService {

    @Autowired
    SongRepository songRepository;

    @Autowired
    UserRepository userRepository;

    /* @Autowired
    PlaylistRepository playlistRepository; */

    @Override
    public void createSong(User user, CreateSongRequestDTO song) {
        // Verificamos si el usuario tiene permisos para crear la canción
        if (!user.canCreateSong()) {
            // Si no tiene permisos, lanzamos una excepción
            throw new UnsupportedOperationException("Este usuario no tiene permisos para crear canciones.");
        }

        MusicArtistUser musicArtistUser = (MusicArtistUser) user;
        Song newSong = new Song();
        newSong.setName(song.getName());
        newSong.setGenre(song.getGenre());

        // Verificar si duration no es null antes de asignarlo
        if (song.getDuration() != null) {
            newSong.setDuration(song.getDuration());
        } else {
            // Puedes asignar un valor por defecto si lo deseas
            newSong.setDuration(0); // o cualquier valor que tenga sentido
        }

        newSong.setMusicArtistUser(musicArtistUser);

        // Guardamos la canción en la base de datos
        songRepository.save(newSong);
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
public void updateSong(Long userId, Long songId, UpdateSongRequestDTO song) throws Exception {
    // Buscar la canción en la base de datos
    Song songDB = songRepository.findById(songId)
            .orElseThrow(() -> new Exception("❌ Canción no encontrada con ID: " + songId));

    // 🔍 Verificar si la encontró correctamente
    System.out.println("✅ Canción encontrada: ID=" + songDB.getId() + ", Nombre=" + songDB.getName());

    // Verificar si el usuario es el dueño de la canción
    if (!songDB.getMusicArtistUser().getId().equals(userId)) {
        throw new Exception("🚫 El usuario no es el dueño de esta canción.");
    }

    // Validaciones adicionales
    if (song.getName() == null || song.getName().trim().isEmpty()) {
        throw new Exception("⚠️ El nombre de la canción no puede ser vacío.");
    }
    if (song.getGenre() == null) {
        throw new Exception("⚠️ El género de la canción no puede ser nulo.");
    }
    if (song.getDuration() <= 0) {
        throw new Exception("⚠️ La duración de la canción debe ser mayor a 0.");
    }

    // Actualizar los datos de la canción
    songDB.setName(song.getName());
    songDB.setGenre(song.getGenre());
    songDB.setDuration(song.getDuration());

    // 📌 Antes de guardar, verificar si el ID sigue siendo el mismo
    System.out.println("🛠️ Antes de guardar - ID de la canción: " + songDB.getId());

    // Guardar los cambios en la base de datos
    songRepository.save(songDB);

    // 📌 Después de guardar, verificar si el ID cambió (si cambió, se creó una nueva canción)
    System.out.println("✅ Después de guardar - ID de la canción: " + songDB.getId());
}






    @Override
    public List<Song> getAll() {
        return songRepository.findAll();
    }

    @Override
    public List<Song> getCreatedSongsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("El usuario no existe."));

        if (!user.canCreateSong()) {
            throw new IllegalArgumentException("El usuario no tiene permisos para crear canciones.");
        }

        // Dado que sabemos que es un artista, podemos acceder a las canciones creadas
        return songRepository.findAllByMusicArtistUserId(userId);
    }

    @Override
    public Song findById(Long SongId) {
        return songRepository.findById(SongId)
                .orElseThrow(() -> new RuntimeException("Cancion no encontrada")); // Maneja el caso de no encontrar al
                                                                                   // usuario
    }

    @Override
    public Song getSongById(Long songId) throws RuntimeException {
        return songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));
    }

    @Override
    public List<Song> getFilteredSongs(String artist, Genre genre) {
        if (artist != null && genre != null) {
            return songRepository.findByMusicArtistUser_ArtisticNameAndGenre(artist, genre);
        } else if (artist != null) {
            return songRepository.findByMusicArtistUser_ArtisticName(artist);
        } else if (genre != null) {
            return songRepository.findByGenre(genre);
        } else {
            return songRepository.findAll();
        }
    }

}
