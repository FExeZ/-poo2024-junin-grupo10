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
import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateEnthusiastRequestDto;
import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import ar.edu.unnoba.proyecto_poo_2024.Services.AuthenticationService;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicEnthusiastUserService;

@RestController
@RequestMapping("/users/enthusiast")
public class MusicEnthusiastUserController {
    @Autowired
    MusicEnthusiastUserService musicEnthusiastUserService;
    @Autowired
    AuthenticationService authenticationService;

    @SuppressWarnings("null")
    @PostMapping("/registrar")
    public ResponseEntity<?> createOneUser(@RequestBody CreateEnthusiastRequestDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        MusicEnthusiastUser enthusiastUser = modelMapper.map(userDto, MusicEnthusiastUser.class);
        try {
            musicEnthusiastUserService.createUser(enthusiastUser);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @SuppressWarnings("null")
    @PutMapping("/{id}")
    public ResponseEntity<MusicEnthusiastUser> updateMusicEnthusiastUser(@PathVariable Long id,
            @RequestBody CreateEnthusiastRequestDto MusicEnthusiastUserDetails)
            throws Exception {
        if (musicEnthusiastUserService.findById(id).isPresent()) {
            ModelMapper mapper = new ModelMapper();
            try {
                MusicEnthusiastUser musicEnthusiastUserDB = mapper.map(MusicEnthusiastUserDetails,
                        MusicEnthusiastUser.class);
                musicEnthusiastUserService.updateUser(musicEnthusiastUserDB);
                return new ResponseEntity<>(musicEnthusiastUserDB, HttpStatus.ACCEPTED);
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
        // Verificar que el usuario es un "entusiasta"
        if (authenticationRequestDTO.getUserType() == null || !authenticationRequestDTO.getUserType().equals("enthusiast")) {
            return new ResponseEntity<>("Tipo de usuario incorrecto para este endpoint", HttpStatus.BAD_REQUEST);
        }

        try {
            ModelMapper modelMapper = new ModelMapper();
            MusicEnthusiastUser musicEnthusiastUser = modelMapper.map(authenticationRequestDTO, MusicEnthusiastUser.class);
            String token = authenticationService.authenticate(musicEnthusiastUser);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}