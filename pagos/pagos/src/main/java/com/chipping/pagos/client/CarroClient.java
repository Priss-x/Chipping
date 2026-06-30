package com.chipping.pagos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CarroClient {

    private final WebClient webClient;

    public CarroClient(@Value("${carro.service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void vaciarCarro(Long usuarioId) {
        try {
            webClient.delete()
                    .uri("/carro/{usuarioId}/vaciar", usuarioId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            System.out.println("Carro vaciado para usuario " + usuarioId);
        } catch (Exception e) {
            System.err.println("Error al vaciar carro: " + e.getMessage());
        }
    }
}