package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistDetailDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistSummaryDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.UpdatePlaylistRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    PlaylistService playlistService;

    @GetMapping // ENDPOINT PROBADO
    public ResponseEntity<List<PlaylistSummaryDTO>> getAllPlaylists() {
        List<PlaylistSummaryDTO> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/playlist/{playlistId}/details") // ENDPOINT PROBADO
    public ResponseEntity<PlaylistDetailDTO> getPlaylistDetails(@PathVariable Long playlistId) {
        PlaylistDetailDTO playlistDetail = playlistService.getPlaylistDetails(playlistId);
        return ResponseEntity.ok(playlistDetail);
    }

    @PutMapping("/playlist/{playlistId}/user/{userId}") // ENDPOINT PROBADO
    public ResponseEntity<String> changePlaylistDetails(@PathVariable Long playlistId,
            @RequestBody UpdatePlaylistRequestDTO requestDTO,
            @PathVariable Long userId) throws AccessDeniedException {
        try {
            playlistService.updatePlaylistName(playlistId, requestDTO.getName(), userId);
            return ResponseEntity.ok("Playlist updated successfully");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to update this playlist");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Playlist not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Playlist not found");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @DeleteMapping("/user/{userId}/playlist/{playlistId}") // ENDPOINT PROBADO
    public ResponseEntity<String> deletePlaylist(@PathVariable Long userId, @PathVariable Long playlistId) {
        try {
            playlistService.deletePlaylist(playlistId, userId);
            return ResponseEntity.ok("Playlist eliminada exitosamente.");
        } catch (UnsupportedOperationException e) {
            // Si el usuario no tiene permisos, respondemos con un error 403 (Forbidden)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Este usuario no es dueño de esta playlist.");
        } catch (RuntimeException e) {
            // En caso de que la playlist no se encuentre
            if (e.getMessage().contains("Playlist not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Playlist no encontrada.");
            }
            // En caso de otros errores, responder con un 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @DeleteMapping("/playlist/{playlistId}/song/{songId}/user/{userId}") // ENDPOINT PROBADO
    public ResponseEntity<String> deleteSongFromPlaylist(@PathVariable Long playlistId,
            @PathVariable Long songId,
            @PathVariable Long userId) {
        try {
            playlistService.deleteSongFromPlaylist(playlistId, songId, userId);
            return ResponseEntity.ok("Canción eliminada exitosamente de la playlist.");
        } catch (UnsupportedOperationException e) {
            // Si el usuario no tiene permisos, respondemos con un error 403 (Forbidden)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Este usuario no es dueño de esta playlist.");
        } catch (RuntimeException e) {
            // En caso de que la playlist no se encuentres
            if (e.getMessage().contains("Playlist not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Playlist no encontrada.");
            }
            // En caso de que la canción no se encuentre
            if (e.getMessage().contains("Song not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Canción no encontrada.");
            }
            // En caso de otros errores, responder con un 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }

    }

    @GetMapping("/user/{userId}/CurrentPlaylists") // ENDPOINT PROBADO
    public ResponseEntity<?> getCurrentUserPlaylists(@PathVariable Long userId) {
        try {
            // Obtener las playlists del usuario
            List<PlaylistSummaryDTO> playlists = playlistService.getPlaylistsByUser(userId);

            // Si el usuario no tiene playlists, devolver 404
            if (playlists.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No playlists found for this user.");
            }
            return ResponseEntity.ok(playlists);
        } catch (UnsupportedOperationException e) {
            // Si el usuario no tiene acceso o permisos suficientes
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User is not authorized to access this resource.");
        } catch (RuntimeException e) {
            // En caso de otros errores generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the playlists.");
        }
    }

}
