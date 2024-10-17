package ar.edu.unnoba.proyecto_poo_2024.Model;

import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "songs")
@Data
public class Song {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="song_id")
    private Long id;

    private String name;

    @Enumerated (EnumType.STRING)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "music_artist_user_id") //nombre de la columna en la tabla
    private MusicArtistUser musicArtistUser;
}
