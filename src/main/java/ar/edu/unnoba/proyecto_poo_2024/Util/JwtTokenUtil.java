package ar.edu.unnoba.proyecto_poo_2024.Util;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Data;

@Data
@Component
public class JwtTokenUtil implements Serializable {
    public static final String SECRET = "POO2024";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";

    // Método para generar el token
    public String generateToken(String username, Long userId) {
        return TOKEN_PREFIX + JWT.create()
                .withSubject(username)
                .withClaim("userId", userId) // Aquí agregamos el userId al token
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }
    
    // Verifica si el token es válido
    public boolean verify(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            return true;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    // Obtiene el subject (nombre de usuario) del token
    public String getSubject(String token) {
        return JWT.decode(token.replace(TOKEN_PREFIX, "")).getSubject();
    }

    // Obtiene la fecha de expiración del token
    public Date getExpirationDate(String token) {
        return JWT.decode(token.replace(TOKEN_PREFIX, "")).getExpiresAt();
    }

    // Método para extraer el userId desde el token
    public Long extractUserId(String token) {
        // Decodifica el token JWT
        DecodedJWT decodedJWT = JWT.decode(token.replace(TOKEN_PREFIX, ""));
        
        // Extrae el userId del claim 'userId' del payload
        return decodedJWT.getClaim("userId").asLong();  // Devuelve el userId como Long
    }
}
