package com.chiping.productos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 500, message = "El nombre no puede superar los 500 carácteres")
    @Column(nullable = false, length = 500)
    private String nombre;

    @Column(nullable = false)
    private Integer precio;

    private Integer stock;
    private String marca;
    @Column(name = "descripcion_corta")
    private String descripcionCorta;

    @Column(name = "proveedor_id")
    private Long proveedorId;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;



}
