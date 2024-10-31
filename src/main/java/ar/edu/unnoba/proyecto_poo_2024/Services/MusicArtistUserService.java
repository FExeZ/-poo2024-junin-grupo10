package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;

import java.util.Optional;

public interface MusicArtistUserService {
    public void createUser(MusicArtistUser user) throws Exception;
    public boolean canCreateSong();
    public void updateUser(User user) throws Exception;
    public Optional<MusicArtistUser> findById(Long id) throws Exception;

}
