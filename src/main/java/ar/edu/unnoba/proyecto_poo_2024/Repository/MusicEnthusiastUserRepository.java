package ar.edu.unnoba.proyecto_poo_2024.Repository;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicEnthusiastUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicEnthusiastUserRepository extends JpaRepository<MusicEnthusiastUser, Long> {
    MusicEnthusiastUser findByUsername(String username);
}
