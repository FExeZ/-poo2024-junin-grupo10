package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Dto.CreatePlaylistRequestDto;
import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.UpdateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Services.SongService;
import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    SongService songService;

    // obtener todos los usuarios
    @GetMapping // ENDPOINT PROBADO
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @DeleteMapping("/user/{userId}/deletePlaylist") // ENDPOINT PROBADO
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 si no se encuentra el usuario
            // e.getMessage() --> toma el msj del NoSuchEleementExeption del service
        }
    }

    @PostMapping("/user/{userId}/createPlaylist") // ENDPOINT PROBADO
    public ResponseEntity<?> createUserPlaylist(@PathVariable Long userId,
            @RequestBody CreatePlaylistRequestDto playlist) {
        try {
            userService.createUserPlaylist(userId, playlist); // Llama al servicio para crear la playlist
            return new ResponseEntity<>(HttpStatus.CREATED); // Respuesta 201 Created
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // Respuesta 404 Not Found
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // Respuesta 409 Conflict
        }
    }

    @PostMapping("/user/{userId}/createSong") // ENDPOINT PROBADO
    public ResponseEntity<?> createSong(@PathVariable Long userId, @RequestBody CreateSongRequestDTO song) {
        try {
            // Obtener el usuario por ID
            User user = userService.findById(userId);

            // Intentamos crear la canción
            songService.createSong(user, song);

            // Si no hay excepciones, respondemos con éxito
            return ResponseEntity.ok("Canción creada exitosamente.");
        } catch (UnsupportedOperationException e) {
            // Si el usuario no tiene permisos, respondemos con un error 403 (Forbidden)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Este usuario no tiene permisos para crear canciones.");
        } catch (Exception e) {
            // En caso de cualquier otro error, respondemos con un error 500 (Internal
            // Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    @PutMapping("/user/{userId}/songs/{songId}") // ENDPOINT PROBADO
    public ResponseEntity<?> updateSong(
            @PathVariable Long userId,
            @PathVariable Long songId,
            @RequestBody UpdateSongRequestDTO song) {
        try {
            songService.updateSong(userId, songId, song);
            return ResponseEntity.ok("Canción actualizada exitosamente.");
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Este usuario no tiene permisos para actualizar canciones.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/user/{userId}/playlists/{playlistId}/songs/{songId}") // ENDPOINT PROBADO
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId,
            @PathVariable Long songId) throws Exception {

        userService.addSongToPlaylist(userId, playlistId, songId);

        return ResponseEntity.ok("Canción agregada a la playlist exitosamente.");
    }

}
