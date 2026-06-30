package com.chipping.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//recibir datos de inventario

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDTO {
    private Long productoId;
    private Integer cantidad;
    private String estado;
}
