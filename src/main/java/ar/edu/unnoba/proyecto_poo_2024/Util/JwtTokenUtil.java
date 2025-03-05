package ar.edu.unnoba.proyecto_poo_2024.Util;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.Data;

@Data
@Component
public class JwtTokenUtil implements Serializable {
    public static final String SECRET = "POO2024";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";

    public String generateToken(String username, Long userId) {
        return TOKEN_PREFIX + JWT.create()
                .withSubject(username)
                .withClaim("userId", userId) // Aquí agregamos el userId al token
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }
    

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

    public String getSubject(String token) {
        return JWT.decode(token.replace(TOKEN_PREFIX, "")).getSubject();
    }

    public Date getExpirationDate(String token) {
        return JWT.decode(token.replace(TOKEN_PREFIX, "")).getExpiresAt();
    }
}
