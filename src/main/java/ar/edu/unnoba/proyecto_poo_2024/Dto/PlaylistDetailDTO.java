package ar.edu.unnoba.proyecto_poo_2024.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class PlaylistDetailDTO {
    private String name;
    private List<String> songNames;
    private List<Long> songIds;
}