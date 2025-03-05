package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.proyecto_poo_2024.Dto.AuthenticationRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateArtistRequestDto;
import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Services.AuthenticationService;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicArtistUserService;

@RestController
@RequestMapping("/users/artist")
public class MusicArtistUserController {
    @Autowired
    MusicArtistUserService musicArtistUserService;
    @Autowired
    AuthenticationService authenticationService;

    @SuppressWarnings("null")
    @PostMapping("/registrar")
    public ResponseEntity<?> createOneUser(@RequestBody CreateArtistRequestDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        MusicArtistUser artistUser = modelMapper.map(userDto, MusicArtistUser.class);
        try {
            musicArtistUserService.createUser(artistUser);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @SuppressWarnings("null")
    @PutMapping("/{id}")
    public ResponseEntity<MusicArtistUser> updateMusicArtistUser(@PathVariable Long id,
            @RequestBody CreateArtistRequestDto MusicArtistUserDetails)
            throws Exception {
        if (musicArtistUserService.findById(id).isPresent()) {
            ModelMapper mapper = new ModelMapper();
            try {
                MusicArtistUser musicArtistUserDB = mapper.map(MusicArtistUserDetails, MusicArtistUser.class);
                musicArtistUserService.updateUser(musicArtistUserDB);
                return new ResponseEntity<>(musicArtistUserDB, HttpStatus.ACCEPTED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @SuppressWarnings("null")
    @PostMapping(path = "/auth", produces = "application/json")
    public ResponseEntity<?> authentication(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        // Verificar que el usuario es un "artista"
        if (authenticationRequestDTO.getUserType() == null || !authenticationRequestDTO.getUserType().equals("artist")) {
            return new ResponseEntity<>("Tipo de usuario incorrecto para este endpoint", HttpStatus.BAD_REQUEST);
        }

        try {
            ModelMapper modelMapper = new ModelMapper();
            MusicArtistUser musicArtistUser = modelMapper.map(authenticationRequestDTO, MusicArtistUser.class);
            String token = authenticationService.authenticate(musicArtistUser);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }



}
