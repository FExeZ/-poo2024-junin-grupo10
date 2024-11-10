package ar.edu.unnoba.proyecto_poo_2024.Dto;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import lombok.Data;

@Data
public class SongResponseDTO {
    private long id;
    private String name;
    private Genre genre;
}
