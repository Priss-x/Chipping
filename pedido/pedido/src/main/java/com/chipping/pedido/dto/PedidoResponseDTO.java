package com.chipping.pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private Long usuarioId;
    private Long carroId;
    private String estado;
    private Integer total;
    private String direccionEnvio;
    private LocalDateTime fechaCreacion;
    private List<ItemPedidoDTO> items;
}