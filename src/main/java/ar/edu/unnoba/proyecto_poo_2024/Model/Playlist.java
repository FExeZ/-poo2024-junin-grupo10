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

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // Aquí se define el dueño de la relación
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    //al guardar o actualizar una Playlist, automáticamente se guarden o actualicen las Song asociadas.
    //no es seguro pero esa es la logica la cual pienso en este caso.
    @JoinTable(
            name = "playlist_song",  // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "playlist_id"),  // Clave foránea de Playlist
            inverseJoinColumns = @JoinColumn(name = "song_id")  // Clave foránea de Song
    )
    private List<Song> songs;

}
