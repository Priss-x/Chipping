package com.chipping.carrocompra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroResponseDTO {
    private Long id;
    private Long usuarioId;
    private String estado;
    private LocalDateTime fechaCreacion;
    private List<ItemCarroResponseDTO> items;
    private Integer total;
}