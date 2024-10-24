package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import org.springframework.stereotype.Service;

@Service
public interface PlaylistService {

    void createPlaylist(Playlist playlist);
}
