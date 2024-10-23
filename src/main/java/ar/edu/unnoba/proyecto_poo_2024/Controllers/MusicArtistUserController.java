package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateArtistRequestDto;
import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicArtistUserService;

@RestController
@RequestMapping("/users/artist")
public class MusicArtistUserController {
    @Autowired
    MusicArtistUserService musicArtistUserService;

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
}
