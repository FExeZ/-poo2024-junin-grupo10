package ar.edu.unnoba.proyecto_poo_2024.Services;
import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;

import java.util.Optional;

public interface MusicEnthusiastUserService {
    public void createUser(MusicEnthusiastUser user) throws Exception;
    public void updateUser(User user) throws Exception;
    public Optional<MusicEnthusiastUser> findById(Long id) throws Exception;

}
