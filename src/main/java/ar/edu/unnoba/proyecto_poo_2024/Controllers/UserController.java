package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;

import ar.edu.unnoba.proyecto_poo_2024.Dto.CreatePlaylistRequestDto;
import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Services.SongService;
import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;
import ar.edu.unnoba.proyecto_poo_2024.Util.JwtTokenUtil;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    SongService songService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            // Verificar y decodificar el token JWT
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token; // Eliminar "Bearer "
            
            // Extraer el userId como Long desde el token
            Long userId = jwtTokenUtil.extractUserId(jwtToken);  // Extraer userId del token
            
            if (userId == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.UNAUTHORIZED);
            }

            // Obtener la información del usuario
            User user = userService.findById(userId);

            // Si no se encuentra el usuario, devolver 404
            if (user == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }

            // Devolver los detalles del usuario
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (JWTVerificationException e) {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("ID de usuario no válido", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener los datos del usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


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

    @PostMapping("/user/{userId}/createPlaylist")
    public ResponseEntity<?> createUserPlaylist(@PathVariable Long userId, 
                                                @RequestBody CreatePlaylistRequestDto playlist,
                                                @RequestHeader("Authorization") String token) {
        try {
            // Verificar si el token es válido
            if (!jwtTokenUtil.verify(token)) {
                return new ResponseEntity<>("Token no válido", HttpStatus.UNAUTHORIZED);
            }

            // Llamar al servicio para crear la playlist
            userService.createUserPlaylist(userId, playlist); 

            return new ResponseEntity<>(HttpStatus.CREATED);  // Respuesta 201 Created
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);  // 404
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear la playlist", HttpStatus.INTERNAL_SERVER_ERROR);  // 500
        }
    }



    @PostMapping("/user/{userId}/createSong")
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
            // En caso de cualquier otro error, respondemos con un error 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }


    /* @PutMapping("/user/{userId}/songs/{songId}")
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
    } */


    @PostMapping("/user/{userId}/playlists/{playlistId}/songs/{songId}") // ENDPOINT PROBADO
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId,
            @PathVariable Long songId) throws Exception {

        userService.addSongToPlaylist(userId, playlistId, songId);

        return ResponseEntity.ok("Canción agregada a la playlist exitosamente.");
    }

}
