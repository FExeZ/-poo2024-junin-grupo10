package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createUserPlaylist(@PathVariable Long userId, @RequestBody Playlist playlist) {
        try {
            userService.createUserPlaylist(userId, playlist); // Llama al servicio para crear la playlist
            return new ResponseEntity<>(HttpStatus.CREATED); // Respuesta 201 Created
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // Respuesta 404 Not Found
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // Respuesta 409 Conflict
        }
    }
}
