package ar.edu.unnoba.proyecto_poo_2024.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="playlists")
@Data
public class Playlist {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="playlist_id")
    private Long id;

    @Column (name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Aquí se define el dueño de la relación
    private User user;

    @ManyToMany
    @JoinTable(
            name = "playlist_song",  // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "playlist_id"),  // Clave foránea de Playlist
            inverseJoinColumns = @JoinColumn(name = "song_id")  // Clave foránea de Song
    )
    private List<Song> songs;

}
