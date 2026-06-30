package com.chipping.inventario.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
public class NotificacionClient {

    private final WebClient webClient;

    public NotificacionClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8095")
                .build();
    }

    public void crearNotificacion(String tipo, String mensaje) {
        this.webClient.post()
                .uri("/notificaciones")
                .bodyValue(Map.of("tipo", tipo, "mensaje", mensaje))
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        response -> System.out.println("Alerta enviada en segundo plano."),
                        error -> System.err.println("No se pudo avisar a notificacionesb: " + error.getMessage())
                );
    }

    public void dispararAlertaStockBajo(Long productoId) {
        try {
            this.webClient.post()
                    .uri("/notificaciones/stock/{id}", productoId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}