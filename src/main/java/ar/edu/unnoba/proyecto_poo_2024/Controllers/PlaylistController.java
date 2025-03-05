package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistDetailDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.PlaylistSummaryDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.UpdatePlaylistRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Repository.UserRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.PlaylistService;
import ar.edu.unnoba.proyecto_poo_2024.Util.JwtTokenUtil;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired  // 🔹 Esto inyecta JwtTokenUtil
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    UserRepository userRepository;

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

    @GetMapping("/user/playlists")
    public ResponseEntity<?> getCurrentUserPlaylists(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido o ausente.");
            }

            // Extraer el username desde el token JWT
            String username = jwtTokenUtil.getSubject(token);

            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido.");
            }

            // Buscar el usuario por su username
            User user = userRepository.findByUsername(username)
                                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Obtener las playlists del usuario
            List<PlaylistSummaryDTO> playlists = playlistService.getPlaylistsByUser(user.getId());

            if (playlists.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No playlists found for this user.");
            }

            return ResponseEntity.ok(playlists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al recuperar las playlists: " + e.getMessage());
        }
    }



}
