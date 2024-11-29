package ar.edu.unnoba.proyecto_poo_2024.Dto;

import ar.edu.unnoba.proyecto_poo_2024.Model.MusicArtistUser;
import ar.edu.unnoba.proyecto_poo_2024.Model.Enum.Genre;
import lombok.Data;

@Data
public class SongResponseDTO {
    private long id;
    private String name;
    private Genre genre;

    public SongResponseDTO(long id, String name, Genre genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }
}
