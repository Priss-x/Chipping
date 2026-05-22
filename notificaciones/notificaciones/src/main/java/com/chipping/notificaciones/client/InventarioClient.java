package com.chipping.notificaciones.client;

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
        return webClient.get()
                .uri("/api/inventario/producto/{id}", productoId)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }


}
