package ar.edu.unnoba.proyecto_poo_2024;

import ar.edu.unnoba.proyecto_poo_2024.Util.JwtTokenUtil;
import ar.edu.unnoba.proyecto_poo_2024.Util.PasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProyectoPoo2024Application {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoPoo2024Application.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder();
	}

	@Bean
	public JwtTokenUtil jwtTokenUtil() {
		return new JwtTokenUtil();
	}
}
