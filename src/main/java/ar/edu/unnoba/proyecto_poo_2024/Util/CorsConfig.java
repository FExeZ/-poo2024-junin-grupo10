package ar.edu.unnoba.proyecto_poo_2024.Util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // Importa la anotación @NonNull
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) { // Agrega @NonNull aquí
                registry.addMapping("/**") // Aplica CORS a todas las rutas
                        .allowedOrigins("http://127.0.0.1:5500") // Permite solo tu frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                        .allowedHeaders("*") // Permite cualquier header
                        .allowCredentials(true); // Permitir cookies o sesiones si es necesario
            }
        };
    }
}