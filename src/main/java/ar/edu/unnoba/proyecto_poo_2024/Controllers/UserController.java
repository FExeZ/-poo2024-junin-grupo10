package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicArtistUserService;
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

    //obtener todos los usuarios
    @GetMapping
    public List<User> getAllUsers (){
        return userService.getUsers();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);  // 404 si no se encuentra el usuario
            // e.getMessage() --> toma el msj del NoSuchEleementExeption del service
        }
    }

    @PostMapping("/playlists/{userId}")
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

    @PostMapping("/{userId}/createSong")
    public ResponseEntity<?> createSong(@PathVariable Long userId, @RequestBody Song song) {
        try {
            // Obtener el usuario por ID
            User user = userService.findById(userId);

            // Intentamos crear la canción
            songService.createSong(user, song);

            // Si no hay excepciones, respondemos con éxito
            return ResponseEntity.ok("Canción creada exitosamente.");
        } catch (UnsupportedOperationException e) {
            // Si el usuario no tiene permisos, respondemos con un error 403 (Forbidden)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Este usuario no tiene permisos para crear canciones.");
        } catch (Exception e) {
            // En caso de cualquier otro error, respondemos con un error 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    @PostMapping("/{userId}/playlists/{playlistId}/songs")
    public ResponseEntity<String> addSongToPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId,
            @RequestBody Song song) throws Exception {

        userService.addSongToPlaylist(userId, playlistId, song);

        return ResponseEntity.ok("Canción agregada a la playlist exitosamente.");
    }
}
