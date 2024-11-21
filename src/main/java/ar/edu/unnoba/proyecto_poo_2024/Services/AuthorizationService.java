package ar.edu.unnoba.proyecto_poo_2024.Services;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.proyecto_poo_2024.Model.User;

@Service
public interface AuthorizationService {
    User authorize(String token) throws Exception;
}
