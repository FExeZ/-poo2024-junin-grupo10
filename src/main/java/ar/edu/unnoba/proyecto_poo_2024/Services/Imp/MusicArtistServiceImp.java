package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Repository.MusicArtistUserRepository;
import ar.edu.unnoba.proyecto_poo_2024.Services.MusicArtistUserService;
import ar.edu.unnoba.proyecto_poo_2024.Util.PasswordEncoder;

import java.util.Optional;

@Service
public class MusicArtistServiceImp implements MusicArtistUserService {

    @Autowired
    private MusicArtistUserRepository musicArtistUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(MusicArtistUser artistUser) throws Exception {
        MusicArtistUser artistUserDB = musicArtistUserRepository
                .findByUsername(artistUser.getUsername());
        if (artistUserDB != null) {
            throw new Exception();
        }
        artistUser.setPassword(passwordEncoder.encode(artistUser.getPassword()));
        musicArtistUserRepository.save(artistUser);
    }

    @Override
    public boolean canCreateSong() {
        return true;
    }

    @Override
    public void updateUser(User user) throws Exception {
        MusicArtistUser musicArtisUserDB = musicArtistUserRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        musicArtisUserDB.setUsername(user.getUsername());
        musicArtisUserDB.setPassword(passwordEncoder.encode(user.getPassword()));
        musicArtistUserRepository.save(musicArtisUserDB);
    }

    @Override
    public Optional<MusicArtistUser> findById(Long id) throws Exception {
        return musicArtistUserRepository.findById(id);

    }
}
