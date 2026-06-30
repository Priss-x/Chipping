package com.chipping.inventario.service;

import com.chipping.inventario.client.NotificacionClient;
import com.chipping.inventario.dto.InventarioRequestDTO;
import com.chipping.inventario.dto.StockResponseDTO;
import com.chipping.inventario.model.Inventario;
import com.chipping.inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final NotificacionClient notificacionClient;

    public List<StockResponseDTO> listarTodo() {
        return inventarioRepository.findAll().stream()
                .map(inventario -> {
                    String estado = (inventario.getCantidad() <= 0) ? "AGOTADO" : "DISPONIBLE";
                    return new StockResponseDTO(
                            inventario.getProductoId(),
                            inventario.getCantidad(),
                            estado
                    );
                })
                .collect(Collectors.toList());
    }

    public Optional<Inventario> obtenerStock(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    public boolean descontarStock(Long productoId, Integer cantidad) {
        return inventarioRepository.findByProductoId(productoId)
                .map(inventario -> {
                    int stockActual = inventario.getCantidad();

                    if (stockActual < cantidad) {
                        try {
                            notificacionClient.crearNotificacion(
                                    "ERROR",
                                    "Intento de compra sin stock para producto " + productoId
                            );
                        } catch (Exception ignored) {}
                        return false;
                    }

                    int nuevoStock = stockActual - cantidad;
                    inventario.setCantidad(nuevoStock);

                    try {
                        inventarioRepository.save(inventario);
                        System.out.println("  Stock descontado con éxito en XAMPP.");
                    } catch (Exception e) {
                        System.err.println("Error con BD al descontar: " + e.getMessage());
                        return true;
                    }

                    if (nuevoStock <= 5 && nuevoStock > 0) {
                        try {
                            notificacionClient.crearNotificacion(
                                    "STOCK",
                                    "Producto " + productoId + " con stock bajo: " + nuevoStock
                            );
                        } catch (Exception ignored) {}
                    }

                    return true;
                })
                .orElseGet(() -> {
                    try {
                        notificacionClient.crearNotificacion(
                                "ERROR",
                                "Producto no existe en inventario: " + productoId
                        );
                    } catch (Exception ignored) {}
                    return false;
                });
    }

    public StockResponseDTO crearStock(InventarioRequestDTO request) {
        if (inventarioRepository.findByProductoId(request.getProductoId()).isPresent()) {
            throw new RuntimeException("Ya existe un registro de inventario " +
                    "para el producto " + request.getProductoId());
        }

        Inventario inventario = new Inventario();
        inventario.setProductoId(request.getProductoId());
        inventario.setCantidad(request.getCantidad());

        Inventario guardado = inventarioRepository.save(inventario);

        String estado = (guardado.getCantidad() <= 0) ? "AGOTADO" : "DISPONIBLE";
        return new StockResponseDTO(
                guardado.getProductoId(),
                guardado.getCantidad(),
                estado
        );
    }

    public boolean reponerStock(Long productoId, Integer cantidad) {
        return inventarioRepository.findByProductoId(productoId)
                .map(inventario -> {
                    int stockActual = inventario.getCantidad();
                    int nuevoStock = stockActual + cantidad;

                    inventario.setCantidad(nuevoStock);

                    try {
                        inventarioRepository.save(inventario);
                        System.out.println(" Stock repuesto con éxito en XAMPP (+ " + cantidad + ").");
                        return true;
                    } catch (Exception e) {
                        System.err.println("Error con BD al reponer: " + e.getMessage());
                        return false;
                    }
                })
                .orElse(false);
    }
}