package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistDetailDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistSummaryDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public interface PlaylistService {
    void createPlaylist(Playlist playlist);

    List<PlaylistSummaryDTO> getAllPlaylists();

    PlaylistDetailDTO getPlaylistDetails(Long playlistId);

    void updatePlaylistName(Long playlistId, String newName, Long userId) throws AccessDeniedException;

    void deletePlaylist(Long playlistId, Long userId);

    void addSongToPlaylist(Long playlistId, Long songId, Long userId);

    void deleteSongFromPlaylist(Long playlistId, Long songId, Long userId);

    List<PlaylistSummaryDTO> getPlaylistsByUser(Long userId);
}
