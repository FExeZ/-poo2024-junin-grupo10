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

    @DeleteMapping("/{songId}/user/{userId}")
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


    @GetMapping
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
}
