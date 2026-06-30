package com.chipping.resenas.client;

import com.chipping.resenas.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(@Value("${productos.service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public ProductoDTO obtenerProducto(Long productoId) {
        try {
            return webClient.get()
                    .uri("/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
            return null;
        }
    }
}