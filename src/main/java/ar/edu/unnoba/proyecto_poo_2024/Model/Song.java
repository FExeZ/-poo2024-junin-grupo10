package ar.edu.unnoba.proyecto_poo_2024.Model;

import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "songs")
@Data
public class Song {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="song_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated (EnumType.STRING)
    @Column (name = "genre", nullable = false)
    private Genre genre;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)    //dsp modificar, enrealidad esta como false el optional
    @JoinColumn(name = "music_artist_user_id") //nombre de la columna en la tabla
    private MusicArtistUser musicArtistUser;
}
