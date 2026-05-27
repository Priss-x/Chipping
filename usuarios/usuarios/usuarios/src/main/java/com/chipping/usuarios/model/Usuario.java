package com.chipping.usuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ingresar un formato de correo válido (ej: nombre@correo.com)")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 4, max= 10, message = "La contraseña debe tener entre 4 y 10 caracteres")
    @Column(nullable = false)
    private String password;

    private String role = "CLIENTE";

}
