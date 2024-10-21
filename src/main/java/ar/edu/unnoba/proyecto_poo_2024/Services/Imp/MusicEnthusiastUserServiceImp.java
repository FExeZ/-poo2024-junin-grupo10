package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Repository.MusicEnthusiastUserRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicEnthusiastUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicEnthusiastUserServiceImp implements MusicEnthusiastUserService {
    @Autowired
    MusicEnthusiastUserRepository musicEnthusiastUserRepository;

    @Override
    public MusicEnthusiastUser createUser(MusicEnthusiastUser user) {
        return musicEnthusiastUserRepository.save(user);
    }
}
