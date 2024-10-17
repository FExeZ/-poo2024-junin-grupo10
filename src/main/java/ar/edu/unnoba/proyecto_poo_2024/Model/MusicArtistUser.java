package ar.edu.unnoba.proyecto_poo_2024.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class MusicArtistUser extends User {
    private String artisticName;

    @OneToMany(mappedBy = "musicArtistUser")
    private List<Song> songs;

    public Boolean canCreateSongs(){
        return null;
    }
}
