package com.chiping.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private Integer precio;
    private Integer stock;
    private String marca;
    private String descripcion_corta;
    private String proveedor;
    private String categoriaId;
}
