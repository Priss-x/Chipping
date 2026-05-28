package com.chipping.usuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "El correo no puede estar vacio")
    @Email(message = "Debe ingresar un formato de correo valido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "La contrasena no puede estar vacia")
    @Size(min = 4, max = 10, message = "La contrasena debe tener entre 4 y 10 caracteres")
    @Column(nullable = false)
    private String password;

    private String role = "CLIENTE";
}