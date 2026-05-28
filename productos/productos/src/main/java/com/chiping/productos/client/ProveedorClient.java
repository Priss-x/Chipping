package com.chiping.productos.client;

import com.chiping.productos.dto.ProveedorDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProveedorClient {

    private final WebClient webClient;

    public ProveedorClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ProveedorDTO obtenerProveedor(Long id) {
        return webClient.get()
                .uri("/api/proveedores/{id}", id)
                .retrieve()
                .bodyToMono(ProveedorDTO.class)
                .block();
    }
}

