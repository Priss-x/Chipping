package com.chipping.notificaciones.service;


import com.chipping.notificaciones.client.InventarioClient;
import com.chipping.notificaciones.model.Notificacion;
import com.chipping.notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final InventarioClient inventarioClient;

    public Notificacion guardar (String tipo, String mensaje){
        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(tipo);
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(LocalDateTime.now());

        return notificacionRepository.save(notificacion);
    }

    public void notificarStockBajo(Long productoId) {
        Integer stock = inventarioClient.obtenerStock(productoId);

        if (stock != null && stock < 5) {
            notificacionRepository.save(new Notificacion(
                    null,
                    "STOCK",
                    "Stock bajo para producto: " + productoId,
                    LocalDateTime.now()
            ));
        }
    }
}
