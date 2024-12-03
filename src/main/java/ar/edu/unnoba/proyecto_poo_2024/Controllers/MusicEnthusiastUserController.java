package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Dto.AuthenticationRequestDTO;
import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateEnthusiastRequestDto;
import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import ar.edu.unnoba.proyecto_poo_2024.Services.AuthenticationService;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicEnthusiastUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

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
        ModelMapper modelMapper = new ModelMapper();
        MusicArtistUser musicArtistUser = modelMapper.map(authenticationRequestDTO, MusicArtistUser.class);
        try {
            String token = authenticationService.authenticate(musicArtistUser);
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("token", token);
            return new ResponseEntity<>(token, null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}