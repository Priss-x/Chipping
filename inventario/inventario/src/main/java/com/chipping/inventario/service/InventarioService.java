package com.chipping.inventario.service;

import com.chipping.inventario.entity.Inventario;
import com.chipping.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public Optional<Inventario> obtenerStock(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    public boolean descontarStock(Long productoId, Integer cantidad) {
        return inventarioRepository.findByProductoId(productoId).map(inventario -> {
            if(inventario.getCantidadDisponible() >= cantidad) {
                inventario.setCantidadDisponible(inventario.getCantidadDisponible() - cantidad);
                inventarioRepository.save(inventario);
                return true;
            }
            return false;
        }).orElse(false);
    }

}
