package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Repository.MusicEnthusiastUserRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicEnthusiastUserService;
import ar.edu.unnoba.proyecto_poo_2024.Util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MusicEnthusiastUserServiceImp implements MusicEnthusiastUserService {
    @Autowired
    private MusicEnthusiastUserRepository musicEnthusiastUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(MusicEnthusiastUser enthusiastUser) throws Exception {
        MusicEnthusiastUser enthusiastUserDB = musicEnthusiastUserRepository
                .findByUsername(enthusiastUser.getUsername());
        if (enthusiastUserDB != null) {
            throw new Exception();
        }
        enthusiastUser.setPassword(passwordEncoder.encode(enthusiastUser.getPassword()));
        musicEnthusiastUserRepository.save(enthusiastUser);
    }

    @Override
    public boolean canCreateSong() {
        return false;
    }

    @Override
    public void updateUser(User user) throws Exception {
        MusicEnthusiastUser musicEnthusiastUserDB = musicEnthusiastUserRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        musicEnthusiastUserDB.setUsername(user.getUsername());
        musicEnthusiastUserDB.setPassword(passwordEncoder.encode(user.getPassword()));
        musicEnthusiastUserRepository.save(musicEnthusiastUserDB);
    }

    @Override
    public Optional<MusicEnthusiastUser> findById(Long id) throws Exception {
        return musicEnthusiastUserRepository.findById(id);
    }
}
