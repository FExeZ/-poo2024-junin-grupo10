package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistDetailDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistSummaryDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Repository.PlaylistRepository;
import ar.edu.unnoba.proyecto_poo_2024.Repository.SongRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.PlaylistService;

@Service
public class PlaylistServiceImp implements PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private SongRepository songRepository;

    @Override
    public void createPlaylist(Playlist playlist) {
        // Guardar la playlist sin asociar a un usuario todavía
        playlistRepository.save(playlist);
    }

    @Override
    public List<PlaylistSummaryDTO> getAllPlaylists() {
        // Obtener todas las playlists y mapearlas a DTO con nombre y cantidad de
        // canciones
        return playlistRepository.findAll()
                .stream()
                .map(playlist -> new PlaylistSummaryDTO(
                        playlist.getName(),
                        playlist.getSongs().size() // Contamos las canciones en la playlist
                ))
                .collect(Collectors.toList());
    }

    @Override
    public PlaylistDetailDTO getPlaylistDetails(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // Extraer los nombres de las canciones de la playlist
        List<String> songNames = playlist.getSongs().stream()
                .map(song -> song.getName())
                .collect(Collectors.toList());

        // Retornar los detalles en un DTO usando el constructor generado por Lombok
        return new PlaylistDetailDTO(playlist.getName(), songNames);
    }

    @Override
    public void updatePlaylistName(Long playlistId, String newName, Long userId) throws AccessDeniedException {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // Validar si el usuario es el creador
        if (!playlist.getUser().getId().equals(userId)) { // Asegúrate de que esto sea la validación correcta
            throw new AccessDeniedException("User not authorized to update this playlist"); // Cambié a
                                                                                            // AccessDeniedException
        }

        playlist.setName(newName);
        playlistRepository.save(playlist);
    }

    @Override
    public void deletePlaylist(Long playlistId, Long userId) {
        // Buscar la playlist por ID
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // Verificar que el usuario es el dueño de la playlist
        if (!playlist.getUser().getId().equals(userId)) {
            throw new UnsupportedOperationException("Este usuario no es dueño de esta playlist.");
        }

        // Desvincular las canciones de la playlist (sin borrarlas)
        playlist.getSongs().clear(); // Esto desvincula las canciones de la playlist

        // Guardar los cambios de desvinculación
        playlistRepository.save(playlist);

        // Finalmente, eliminar la playlist
        playlistRepository.delete(playlist);
    }

    @Override
    public void addSongToPlaylist(Long playlistId, Long songId, Long userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // Validar si el usuario es el creador de la playlist
        if (!playlist.getUser().getId().equals(userId)) {
            throw new UnsupportedOperationException("User not authorized to add song to this playlist");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        // Agregar la canción a la playlist
        playlist.getSongs().add(song);
        playlistRepository.save(playlist);
    }

    @Override
    public void deleteSongFromPlaylist(Long playlistId, Long songId, Long userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // Validar si el usuario es el creador de la playlist
        if (!playlist.getUser().getId().equals(userId)) {
            throw new UnsupportedOperationException("User not authorized to remove songs from this playlist");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        // Agregar la canción a la playlist
        playlist.getSongs().remove(song);
        playlistRepository.save(playlist);
    }

    @Override
    public List<PlaylistSummaryDTO> getPlaylistsByUser(Long userId) {
        // Obtener las playlists asociadas al usuario desde el repositorio
        List<Playlist> playlists = playlistRepository.findByUserId(userId);

        // Mapear las playlists a DTOs para la respuesta
        return playlists.stream()
                .map(playlist -> new PlaylistSummaryDTO(
                        playlist.getName(),
                        playlist.getSongs().size() // Contamos las canciones en la playlist
                ))
                .collect(Collectors.toList());
    }

}
