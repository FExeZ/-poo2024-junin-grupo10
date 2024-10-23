package ar.edu.unnoba.proyecto_poo_2024.Dto;

import lombok.Data;

@Data
public class CreateArtistRequestDto {
    private String username;
    private String password;
    private String artisticName;
}
