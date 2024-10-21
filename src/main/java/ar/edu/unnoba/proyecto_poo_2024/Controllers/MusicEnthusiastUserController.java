package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicEnthusiastUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/enthusiast")
public class MusicEnthusiastUserController {
    @Autowired
    MusicEnthusiastUserService musicEnthusiastUserService;

    @PostMapping("/registrar")
    public ResponseEntity<MusicEnthusiastUser> createOneUser(@RequestBody MusicEnthusiastUser musicEnthusiastUser) {
        MusicEnthusiastUser newUser = musicEnthusiastUserService.createUser(musicEnthusiastUser);
        return ResponseEntity.ok(newUser);

    }
}