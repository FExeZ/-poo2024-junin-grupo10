package ar.edu.unnoba.proyecto_poo_2024.Services;

import ar.edu.unnoba.proyecto_poo_2024.Dto.CreatePlaylistRequestDto;
import ar.edu.unnoba.proyecto_poo_2024.Model.Song;
import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getUsers();

    void canCreateSong(User user, Song song);

    void deleteUser(Long userId);

    void createUserPlaylist(Long userId, CreatePlaylistRequestDto playlist);

    User findByUsername(String username);

    User findById(Long userId);

    void addSongToPlaylist(Long userId, Long playlistId, Long songId) throws Exception;
}
