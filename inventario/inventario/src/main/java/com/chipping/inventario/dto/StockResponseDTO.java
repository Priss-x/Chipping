package com.chipping.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDTO {
    private Long productoId;
    private Integer cantidad;
    private String estado;
}
