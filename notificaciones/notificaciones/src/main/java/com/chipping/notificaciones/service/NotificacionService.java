package com.chipping.notificaciones.service;

import com.chipping.notificaciones.client.InventarioClient;
import com.chipping.notificaciones.dto.NotificacionResponseDTO;
import com.chipping.notificaciones.model.Notificacion;
import com.chipping.notificaciones.model.TipoNotificacion;
import com.chipping.notificaciones.repository.NotificacionRepository;
import com.chipping.notificaciones.repository.TipoNotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final InventarioClient inventarioClient;

    public List<NotificacionResponseDTO> listarTodas() {
        return notificacionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public NotificacionResponseDTO guardar(String tipoNombre, String mensaje) {
        TipoNotificacion tipo = tipoNotificacionRepository.findByNombre(tipoNombre)
                .orElseThrow(() -> new RuntimeException("El tipo de notificación '" + tipoNombre + "' no existe"));

        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(tipo);
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(LocalDateTime.now());

        return mapToDTO(notificacionRepository.save(notificacion));
    }

    public void notificarStockBajo(Long productoId) {
        Integer stock = inventarioClient.obtenerStock(productoId);

        if (stock != null && stock < 5) {
            TipoNotificacion tipoStock = tipoNotificacionRepository.findByNombre("STOCK")
                    .orElseThrow(() -> new RuntimeException("El tipo de notificación 'STOCK' no existe"));

            Notificacion notificacion = new Notificacion();
            notificacion.setTipo(tipoStock);
            notificacion.setMensaje("Stock bajo para producto: " + productoId);
            notificacion.setFecha(LocalDateTime.now());

            notificacionRepository.save(notificacion);
        }
    }

    private NotificacionResponseDTO mapToDTO(Notificacion n) {
        return new NotificacionResponseDTO(
                n.getId(),
                n.getTipo().getId(),
                n.getTipo().getNombre(),
                n.getMensaje(),
                n.getFecha()
        );
    }
}