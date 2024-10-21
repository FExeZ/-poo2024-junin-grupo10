package ar.edu.unnoba.proyecto_poo_2024.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("AU")
@EqualsAndHashCode(callSuper = false)
public class MusicArtistUser extends User {

    @Column (name = "artistic_name", nullable = true)
    private String artisticName;

    @OneToMany(mappedBy = "musicArtistUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //pienso cascade all para que no queden canciones huerfanas
    private List<Song> songs;

    public Boolean canCreateSongs(){
        return true;
    }
}
