package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public ResponseEntity<List<SongResponseDTO>> getAllSongs() {
        try {
            // Obtener todas las canciones
            List<Song> songs = songService.getAll();

            // Mapear las canciones a DTOs
            ModelMapper modelMapper = new ModelMapper();
            List<SongResponseDTO> songResponseDTOs = songs.stream()
                    .map(song -> modelMapper.map(song, SongResponseDTO.class))
                    .collect(Collectors.toList());

            // Retornar las canciones con estado 200 OK
            return new ResponseEntity<>(songResponseDTOs, HttpStatus.OK);
        } catch (Exception e) {
            // En caso de error, retornar estado 500
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@GetMapping
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
    }*/
}
