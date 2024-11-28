package ar.edu.unnoba.proyecto_poo_2024.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class PlaylistDetailDTO {
    private String name;
    private List<String> songNames;
}