package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistDetailDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistSummaryDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import ar.edu.unnoba.proyecto_poo_2024.Repository.PlaylistRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImp implements PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Override
    public void createPlaylist(Playlist playlist) {
        // Guardar la playlist sin asociar a un usuario todavía
        playlistRepository.save(playlist);
    }

    public List<PlaylistSummaryDTO> getAllPlaylists() {
        // Obtener todas las playlists y mapearlas a DTO con nombre y cantidad de canciones
        return playlistRepository.findAll()
                .stream()
                .map(playlist -> new PlaylistSummaryDTO(
                        playlist.getName(),
                        playlist.getSongs().size() // Contamos las canciones en la playlist
                ))
                .collect(Collectors.toList());
    }

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
}


