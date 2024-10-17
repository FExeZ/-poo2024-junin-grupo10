package ar.edu.unnoba.proyecto_poo_2024.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("EU")
public class MusicEnthusiastUser extends User{
    public Boolean canCreateSongs(){
        return null;
    }
}
