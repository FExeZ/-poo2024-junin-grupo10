package ar.edu.unnoba.proyecto_poo_2024.Repository;

import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findByUserId(Long userId);
}
