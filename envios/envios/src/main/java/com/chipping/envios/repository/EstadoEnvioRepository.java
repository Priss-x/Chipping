package com.chipping.envios.repository;

import com.chipping.envios.model.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoEnvioRepository extends JpaRepository<EstadoEnvio, Long> {
    Optional<EstadoEnvio> findByNombre(String nombre);
}