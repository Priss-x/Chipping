package com.chipping.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionResponseDTO {
    private Long id;
    private Long pedidoId;
    private Long usuarioId;
    private Integer monto;
    private String metodoPago;
    private String estado;
    private String codigoAutorizacion;
    private LocalDateTime fechaCreacion;
}