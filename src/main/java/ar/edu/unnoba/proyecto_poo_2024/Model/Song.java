package ar.edu.unnoba.proyecto_poo_2024.Model;

import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "songs")
@Data
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY) // dsp modificar, enrealidad esta como false el optional
    @JoinColumn(name = "music_artist_user_id", foreignKey = @ForeignKey(name = "fk_artist_user_id"))
    @JsonBackReference
    private MusicArtistUser musicArtistUser;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Playlist> playlists;
}
