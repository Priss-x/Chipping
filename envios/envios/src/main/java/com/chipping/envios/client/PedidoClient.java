package com.chipping.envios.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PedidoClient {

    private final WebClient webClient;

    public PedidoClient(@Value("${pedido.service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void actualizarEstado(Long pedidoId, String nuevoEstado) {
        try {
            webClient.patch()
                    .uri("/pedidos/{id}/estado?nuevoEstado={estado}", pedidoId, nuevoEstado)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe(
                            response -> System.out.println("Estado pedido actualizado a " + nuevoEstado),
                            error -> System.err.println("No se pudo actualizar estado: " + error.getMessage())
                    );
        } catch (Exception e) {
            System.err.println("Error al notificar a pedido: " + e.getMessage());
        }
    }
}