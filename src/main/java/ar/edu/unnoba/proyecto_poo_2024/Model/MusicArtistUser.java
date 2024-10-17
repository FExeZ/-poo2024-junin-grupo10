package ar.edu.unnoba.proyecto_poo_2024.Model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("AS")
public class MusicArtistUser extends User {

    @Column (name = "artistic_name", nullable = true)
    private String artisticName;

    @OneToMany(mappedBy = "musicArtistUser")
    private List<Song> songs;

    public Boolean canCreateSongs(){
        return null;
    }
}
