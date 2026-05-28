package com.chipping.carrocompra.dto;

import lombok.Data;

@Data
public class StockDTO {
    private Long productoId;
    private Integer cantidad;
    private String estado;
}
