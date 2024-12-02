package ar.edu.unnoba.proyecto_poo_2024.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@DiscriminatorValue("AU")
@EqualsAndHashCode(callSuper = false)
public class MusicArtistUser extends User {

    @Column(name = "artistic_name", nullable = true)
    private String artisticName;

    @OneToMany(mappedBy = "musicArtistUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Song> songs;

    @Override
    public boolean canCreateSong() {
        return true;
    }
}
