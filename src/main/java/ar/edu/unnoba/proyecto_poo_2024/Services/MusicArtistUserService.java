package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;

public interface MusicArtistUserService {
    public void createUser(MusicArtistUser user) throws Exception;

    public boolean canCreateSong();

}
