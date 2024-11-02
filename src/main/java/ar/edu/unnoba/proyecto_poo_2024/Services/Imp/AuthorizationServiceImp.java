package ar.edu.unnoba.proyecto_poo_2024.Services.Imp;

import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Services.AuthorizationService;
import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;
import ar.edu.unnoba.proyecto_poo_2024.Util.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImp implements AuthorizationService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;

    @Override
    public User authorize(String token) throws Exception {
        if (!jwtTokenUtil.verify(token))
            throw new Exception();
        String username = jwtTokenUtil.getSubject(token);
        User user = userService.findByUsername(username);
        if (user == null)
            throw new Exception();
        return user;
    }
}
