package com.chipping.notificaciones.repository;

import com.chipping.notificaciones.model.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Long> {
    Optional<TipoNotificacion> findByNombre(String nombre);
}