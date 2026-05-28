package com.chipping.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Long id;
    private Long usuarioId;
    private Integer total;
    private String estado;
    private List<ItemPedidoDTO> items;
}