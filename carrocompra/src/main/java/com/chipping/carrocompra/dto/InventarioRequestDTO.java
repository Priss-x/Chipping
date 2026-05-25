package com.chipping.carrocompra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioRequestDTO {
    private Long productoId;
    private Integer cantidad;
}
