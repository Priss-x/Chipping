package com.chipping.envios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioResponseDTO {
    private Long id;
    private Long pedidoId;
    private Long usuarioId;
    private String direccionDestino;
    private String estado;
    private String numeroSeguimiento;
    private LocalDate fechaEstimada;
    private LocalDateTime fechaCreacion;
}