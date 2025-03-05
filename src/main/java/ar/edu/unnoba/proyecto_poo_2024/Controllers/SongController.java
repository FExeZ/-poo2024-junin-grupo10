package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.proyecto_poo_2024.Dto.SongResponseDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.UpdateSongRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
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
            //e.printStackTrace();  Agrega esta línea para imprimir la excepción
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

    @GetMapping("/song/{songId}") // ENDPOINT PROBADO
    public ResponseEntity<?> getSongById(@PathVariable Long songId) {
        try {
            // Llamar al servicio para obtener la canción por su ID
            Song songDB = songService.getSongById(songId);

            // Mapear la canción a un DTO para la respuesta
            ModelMapper modelMapper = new ModelMapper();
            SongResponseDTO songResponseDTO = modelMapper.map(songDB, SongResponseDTO.class);

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

    @GetMapping("/filter") // ENDPOINT PROBADO
    public ResponseEntity<?> getSongs(
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) Genre genre) {

        try {
            // Llamar al servicio para obtener las canciones filtradas
            List<Song> songs = songService.getFilteredSongs(artist, genre);

            // Mapear las canciones a una lista de DTOs
            ModelMapper modelMapper = new ModelMapper();
            List<SongResponseDTO> songResponseDTOs = songs.stream()
                    .map(song -> modelMapper.map(song, SongResponseDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(songResponseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las canciones.");
        }
    }

}
