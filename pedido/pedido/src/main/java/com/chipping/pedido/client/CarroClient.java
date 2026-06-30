package com.chipping.pedido.client;

import com.chipping.pedido.dto.CarroDTO;
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

    public CarroDTO obtenerCarro(Long usuarioId) {
        try {
            return webClient.get()
                    .uri("/carro/{usuarioId}", usuarioId)
                    .retrieve()
                    .bodyToMono(CarroDTO.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Error al obtener carro desde Pedido: " + e.getMessage());
            return null;
        }
    }
}