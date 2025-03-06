package ar.edu.unnoba.proyecto_poo_2024.Dto;

import lombok.Data;

@Data
public class AuthenticationResponseDTO {
    private String token;  // El token de autenticación
    private Long userId;   // El ID del usuario

    // Getter y Setter para token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getter y Setter para userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
