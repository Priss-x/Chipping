package com.chipping.inventario.repository;

import com.chipping.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario,Long> {
    Optional<Inventario> findByProductoId(Long productoId);
}
