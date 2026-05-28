package com.chipping.resenas.repository;

import com.chipping.resenas.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByProductoId(Long productoId);
    List<Resena> findByUsuarioId(Long usuarioId);
    Optional<Resena> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
}