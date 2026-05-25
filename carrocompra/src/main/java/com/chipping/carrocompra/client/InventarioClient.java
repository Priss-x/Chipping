package com.chipping.carrocompra.client;

import com.chipping.carrocompra.dto.InventarioRequestDTO;
import com.chipping.carrocompra.dto.StockDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class InventarioClient {
    private final WebClient webClient;

    public InventarioClient(WebClient webClient) {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8092")
                .build();
    }

    public StockDTO obtenerStock(Long productoId) {
        try {
            return webClient.get()
                    .uri("/api/inventario/{productoId}", productoId)
                    .retrieve()
                    .bodyToMono(StockDTO.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Error al obtener stock desde Carro: " + e.getMessage());
            return null;
        }
    }

    public boolean descontarStock(Long productoId, Integer cantidad) {
        try {
            InventarioRequestDTO request = new InventarioRequestDTO(productoId, cantidad);
            webClient.post()
                    .uri("/api/inventario/descontar")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            System.out.println("Descuento de stock procesado correctamente en Inventario.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al descontar stock desde Carro: " + e.getMessage());
            return false;
        }
    }

    // 💡 EL MÉTODO QUE FALTABA: Para conectar con el endpoint de reposición en Inventario
    public boolean reponerStock(Long productoId, Integer cantidad) {
        try {
            InventarioRequestDTO request = new InventarioRequestDTO(productoId, cantidad);
            webClient.post()
                    .uri("/api/inventario/reponer")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            System.out.println("Reposición de stock procesada correctamente en Inventario.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al reponer stock desde Carro: " + e.getMessage());
            return false;
        }
    }
}