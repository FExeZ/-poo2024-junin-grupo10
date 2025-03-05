package ar.edu.unnoba.proyecto_poo_2024.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaylistSummaryDTO {
    private Long id;         // Agregar el campo id
    private String name;
    private int songCount;
}
