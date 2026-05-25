package com.chipping.carrocompra.client;

import com.chipping.carrocompra.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(@Value("${productos.service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public ProductoDTO obtenerProducto(Long id) {
        return webClient.get()
                .uri("/productos/{id}", id)
                .retrieve()
                .bodyToMono(ProductoDTO.class)
                .block();
    }
}