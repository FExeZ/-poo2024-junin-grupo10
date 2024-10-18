package ar.edu.unnoba.proyecto_poo_2024.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@DiscriminatorValue("EU")
@EqualsAndHashCode(callSuper = false)
public class MusicEnthusiastUser extends User{
    public Boolean canCreateSongs(){
        return false;
    }
}
