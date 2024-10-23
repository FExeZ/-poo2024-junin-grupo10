package ar.edu.unnoba.proyecto_poo_2024.Controllers;

import ar.edu.unnoba.proyecto_poo_2024.Model.User;
import ar.edu.unnoba.proyecto_poo_2024.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    //obtener todos los usuarios
    @GetMapping
    public List<User> getAllUsers (){
        return userService.getUsers();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);  // 404 si no se encuentra el usuario
            // e.getMessage() --> toma el msj del NoSuchEleementExeption del service
        }
    }

}
