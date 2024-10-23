package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import ar.edu.unnoba.proyecto_poo_2024.Repository.MusicEnthusiastUserRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicEnthusiastUserService;
import ar.edu.unnoba.proyecto_poo_2024.Util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicEnthusiastUserServiceImp implements MusicEnthusiastUserService {
    @Autowired
    private MusicEnthusiastUserRepository musicEnthusiastUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void createUser(MusicEnthusiastUser enthusiastUser) throws Exception{
        MusicEnthusiastUser enthusiastUserDB = musicEnthusiastUserRepository.findByUsername(enthusiastUser.getUsername());
        if (enthusiastUserDB != null){
            throw new Exception();
        }
        enthusiastUser.setPassword(passwordEncoder.encode(enthusiastUser.getPassword()));
        musicEnthusiastUserRepository.save(enthusiastUser);
    }
}
