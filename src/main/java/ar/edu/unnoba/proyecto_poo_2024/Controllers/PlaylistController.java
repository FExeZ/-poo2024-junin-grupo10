package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistDetailDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistSummaryDTO;
import ar.edu.unnoba.proyecto_poo_2024.Services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    PlaylistService playlistService;
    @GetMapping
    public ResponseEntity<List<PlaylistSummaryDTO>> getAllPlaylists() {
        List<PlaylistSummaryDTO> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/details/{playlistId}")
    public ResponseEntity<PlaylistDetailDTO> getPlaylistDetails(@PathVariable Long playlistId) {
        PlaylistDetailDTO playlistDetail = playlistService.getPlaylistDetails(playlistId);
        return ResponseEntity.ok(playlistDetail);
    }
}
