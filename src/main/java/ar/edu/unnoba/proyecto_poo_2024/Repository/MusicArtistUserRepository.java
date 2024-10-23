package ar.edu.unnoba.proyecto_poo_2024.Repository;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicArtistUserRepository extends JpaRepository<MusicArtistUser, Long> {

    MusicArtistUser findByUsername(String username);
}
