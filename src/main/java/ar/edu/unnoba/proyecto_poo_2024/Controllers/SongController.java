package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.unnoba.proyecto_poo_2024.Dto.SongResponseDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.UpdateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Services.AuthorizationService;
import ar.edu.unnoba.proyecto_poo_2024.Services.SongService;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    SongService songService;
    @Autowired
    AuthorizationService authorizationService;

    @DeleteMapping("/{songId}/user/{userId}") // ENDPOINT PROBADO
    public ResponseEntity<String> deleteSong(
            @PathVariable Long songId,
            @PathVariable Long userId) {
        try {
            songService.deleteSongByIdAndUser(songId, userId);
            return ResponseEntity.ok("Canción eliminada exitosamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Agrega esta línea para imprimir la excepción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    @SuppressWarnings("null")
    @GetMapping // ENDPOINT PROBADO
    public ResponseEntity<List<SongResponseDTO>> getAllSongs(@RequestHeader("Authorization") String token) {
        try {
            authorizationService.authorize(token);
            List<Song> songs = songService.getAll();
            ModelMapper modelMapper = new ModelMapper();
            List<SongResponseDTO> songResponseDTOs = songs.stream()
                    .map(song -> modelMapper.map(song, SongResponseDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(songResponseDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/user/{userId}/created-songs") // ENDPOINT PROBADO
    public ResponseEntity<List<Song>> getCreatedSongsByUser(@PathVariable Long userId) {
        try {
            List<Song> songs = songService.getCreatedSongsByUser(userId);
            return ResponseEntity.ok(songs);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/{songId}")
    public ResponseEntity<?> getSongById(@PathVariable Long songId) {
        try {
            // Llamar al servicio para obtener la canción por su ID
            Song song = songService.getSongById(songId);

            // Mapear la canción a un DTO para la respuesta
            ModelMapper modelMapper = new ModelMapper();
            SongResponseDTO songResponseDTO = modelMapper.map(song, SongResponseDTO.class);

            // Retornar la canción en la respuesta
            return ResponseEntity.ok(songResponseDTO);
        } catch (RuntimeException e) {
            // Si la canción no se encuentra, retornar error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Canción no encontrada.");
        }
    }

    @PutMapping("/{id}") // ENDPOINT PROBADO
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
}
