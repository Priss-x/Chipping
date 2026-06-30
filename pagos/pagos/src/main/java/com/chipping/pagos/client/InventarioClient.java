package com.chipping.pagos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class InventarioClient {

    private final WebClient webClient;

    public InventarioClient(@Value("${inventario.service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void descontarStock(Long productoId, Integer cantidad) {
        try {
            webClient.post()
                    .uri("/api/inventario/descontar")
                    .bodyValue(new java.util.HashMap<String, Object>() {{
                        put("productoId", productoId);
                        put("cantidad", cantidad);
                    }})
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            System.err.println("Error al descontar stock: " + e.getMessage());
        }
    }
}