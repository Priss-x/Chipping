package com.chipping.carrocompra.repository;

import com.chipping.carrocompra.model.EstadoCarro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoCarroRepository extends JpaRepository<EstadoCarro, Long> {
    Optional<EstadoCarro> findByNombre(String nombre);
}