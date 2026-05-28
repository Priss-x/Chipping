package com.chipping.carrocompra.repository;

import com.chipping.carrocompra.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {
    Optional<Carro> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
