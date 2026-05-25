package com.chipping.inventario.service;

import com.chipping.inventario.client.NotificacionClient;
import com.chipping.inventario.entity.Inventario;
import com.chipping.inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final NotificacionClient notificacionClient;

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
                        System.out.println("💾 Stock descontado con éxito en XAMPP.");
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

    public boolean reponerStock(Long productoId, Integer cantidad) {
        return inventarioRepository.findByProductoId(productoId)
                .map(inventario -> {
                    int stockActual = inventario.getCantidad();
                    int nuevoStock = stockActual + cantidad;

                    inventario.setCantidad(nuevoStock);

                    try {
                        inventarioRepository.save(inventario);
                        System.out.println("💾 Stock repuesto con éxito en XAMPP (+ " + cantidad + ").");
                        return true;
                    } catch (Exception e) {
                        System.err.println("Error con BD al reponer: " + e.getMessage());
                        return false;
                    }
                })
                .orElse(false);
    }
}