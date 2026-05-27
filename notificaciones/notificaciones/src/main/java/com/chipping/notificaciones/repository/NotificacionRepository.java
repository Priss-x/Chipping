package com.chipping.notificaciones.repository;

import com.chipping.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}
