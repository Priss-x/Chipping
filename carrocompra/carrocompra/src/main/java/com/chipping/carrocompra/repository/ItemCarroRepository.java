package com.chipping.carrocompra.repository;

import com.chipping.carrocompra.model.ItemCarro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCarroRepository extends JpaRepository<ItemCarro, Long> {
    Optional<ItemCarro> findByCarroIdAndProductoId(Long carroId, Long productoId);
}
