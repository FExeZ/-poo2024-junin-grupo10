package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Dto.CreateEnthusiastRequestDto;
import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicEnthusiastUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.modelmapper.ModelMapper;
@RestController
@RequestMapping("/users/enthusiast")
public class MusicEnthusiastUserController {
    @Autowired
    MusicEnthusiastUserService musicEnthusiastUserService;

    @PostMapping("/registrar")
    public ResponseEntity<?> createOneUser(@RequestBody CreateEnthusiastRequestDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        MusicEnthusiastUser enthusiastUser = modelMapper.map(userDto, MusicEnthusiastUser.class);
        try{
            musicEnthusiastUserService.createUser(enthusiastUser);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
}