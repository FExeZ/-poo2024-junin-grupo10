package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Model.Playlist;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getUsers();

    void deleteUser(Long userId);

    void createUserPlaylist(Long userId, Playlist playlist);

    User findByUsername(String username);

}
