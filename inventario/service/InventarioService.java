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
        return inventarioRepository.findByProductoId(productoId).map(inventario -> {
            if(inventario.getCantidadDisponible() >= cantidad) {
                int nuevoStock = inventario.getCantidadDisponible() - cantidad;
                inventario.setCantidadDisponible(nuevoStock);
                inventarioRepository.save(inventario);

                if (nuevoStock <=5) {
                    notificacionClient.crearNotificacion(
                            "STOCK", "producto" + productoId + "con stock bajo: " + nuevoStock
                    );
                }
                return true;
            }
            notificacionClient.crearNotificacion(
                    "ERROR", "Intento de compra sin stock para el producto" + productoId
            );
            return false;

        }).orElseGet(() -> {
            notificacionClient.crearNotificacion(
                    "ERROR", "Producto no existe: " + productoId
            );
            return false;
        });
    }

}
