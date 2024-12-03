package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;

import java.util.Optional;

public interface MusicArtistUserService {
    void createUser(MusicArtistUser user) throws Exception;

    void updateUser(User user) throws Exception;

    Optional<MusicArtistUser> findById(Long id) throws Exception;

}
