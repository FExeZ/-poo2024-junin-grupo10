package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistDetailDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistSummaryDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaylistService {
    void createPlaylist(Playlist playlist);

    List<PlaylistSummaryDTO> getAllPlaylists();

    PlaylistDetailDTO getPlaylistDetails(Long playlistId);
}
