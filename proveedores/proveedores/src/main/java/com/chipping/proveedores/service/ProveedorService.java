package com.chipping.proveedores.service;

import com.chipping.proveedores.dto.ProveedorDTO;
import com.chipping.proveedores.entity.Proveedor;
import com.chipping.proveedores.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public ProveedorDTO obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(new ProveedorDTO(0L, "Proveedor desconocido", "N/A"));
    }

    public Proveedor crear(Proveedor p) {
        return proveedorRepository.save(p);
    }

    public List<ProveedorDTO> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private ProveedorDTO mapToDTO(Proveedor p) {
        return new ProveedorDTO(
                p.getId(),
                p.getNombre(),
                p.getPais()
        );
    }
}