package com.chipping.carrocompra.client;

import com.chipping.carrocompra.dto.ProductoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductoClient {
    private final WebClient webClient;


    public ProductoClient(WebClient webClient) {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8090")
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
            System.err.println("Error al obtener producto desde Carro: " + e.getMessage());
            return null;
        }
    }
}

