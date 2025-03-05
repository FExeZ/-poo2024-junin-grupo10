package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Services.AuthenticationService;
import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;
import ar.edu.unnoba.proyecto_poo_2024.Util.JwtTokenUtil;
import ar.edu.unnoba.proyecto_poo_2024.Util.PasswordEncoder;

@Service
public class AuthenticationServiceImp implements AuthenticationService {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String authenticate(User user) throws Exception {
        // Busca al usuario por nombre de usuario
        User userDB = userService.findByUsername(user.getUsername());
        if (userDB == null) {
            throw new Exception("Usuario no encontrado en la base de datos.");
        }
        
        // Verifica si el tipo de usuario coincide con el tipo esperado
        if (user.getClass() != userDB.getClass()) {
            throw new Exception("Tipo de usuario incorrecto.");
        }

        // Verifica la contraseña
        if (!passwordEncoder.verify(user.getPassword(), userDB.getPassword())) {
            throw new Exception("Contraseña incorrecta.");
        }
        
        // Genera y retorna el token JWT si todo es válido
        return jwtTokenUtil.generateToken(user.getUsername());
    }

}
