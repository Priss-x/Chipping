package com.chipping.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionResponseDTO {
    private Long id;
    private Long tipoId;
    private String tipoNombre;
    private String mensaje;
    private LocalDateTime fecha;
}