package ar.edu.unnoba.proyecto_poo_2024.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

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

    @Column(name = "duration", nullable=true)
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY) // dsp modificar, enrealidad esta como false el optional
    @JoinColumn(name = "music_artist_user_id", foreignKey = @ForeignKey(name = "fk_artist_user_id"))
    @JsonBackReference
    private MusicArtistUser musicArtistUser;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Playlist> playlists;

    public boolean isPresent() {
        return false;
    }
}
