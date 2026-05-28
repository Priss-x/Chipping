package com.chipping.pagos.repository;

import com.chipping.pagos.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    Optional<Transaccion> findByPedidoId(Long pedidoId);
    List<Transaccion> findByUsuarioId(Long usuarioId);
}