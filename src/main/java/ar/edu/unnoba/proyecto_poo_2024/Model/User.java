package ar.edu.unnoba.proyecto_poo_2024.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)

public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Aquí está el lado no dueño de la
                                                                                     // relación
    // el cascade es para que por ejemplo cuando se borre un user entonces se borren
    // las playlists asociadas
    private List<Playlist> playlists;
}
