package com.chipping.notificaciones.client;

import com.chipping.notificaciones.dto.StockResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class InventarioClient {
    private final WebClient webClient;

    public InventarioClient(@Value("${inventario.service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
    public Integer obtenerStock(Long productoId){
        try {
            StockResponseDTO respuesta = webClient.get()
                    .uri("/api/inventario/{id}", productoId)
                    .retrieve()
                    .bodyToMono(StockResponseDTO.class)
                    .block();

            return (respuesta != null) ? respuesta.getCantidad() : 0;
        } catch (Exception e) {
            System.err.println("Error al obtener stock desde Notificaciones: " + e.getMessage());
            return 0;
        }
    }
}


