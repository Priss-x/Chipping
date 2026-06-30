package com.chipping.proveedores.service;

import com.chipping.proveedores.dto.ProveedorDTO;
import com.chipping.proveedores.dto.ProveedorRequestDTO;
import com.chipping.proveedores.model.Pais;
import com.chipping.proveedores.model.Proveedor;
import com.chipping.proveedores.repository.PaisRepository;
import com.chipping.proveedores.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;
    private final PaisRepository paisRepository;

    public ProveedorService(ProveedorRepository proveedorRepository, PaisRepository paisRepository) {
        this.proveedorRepository = proveedorRepository;
        this.paisRepository = paisRepository;
    }

    public ProveedorDTO obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(new ProveedorDTO(0L, "Proveedor desconocido", null, "N/A"));
    }

    public ProveedorDTO crear(ProveedorRequestDTO request) {
        Pais pais = paisRepository.findById(request.getPaisId())
                .orElseThrow(() -> new RuntimeException("El país con id " + request.getPaisId() + " no existe"));

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(request.getNombre());
        proveedor.setPais(pais);

        Proveedor guardado = proveedorRepository.save(proveedor);
        return mapToDTO(guardado);
    }

    public List<ProveedorDTO> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProveedorDTO mapToDTO(Proveedor p) {
        Long paisId = (p.getPais() != null) ? p.getPais().getId() : null;
        String paisNombre = (p.getPais() != null) ? p.getPais().getNombre() : "Desconocido";
        return new ProveedorDTO(p.getId(), p.getNombre(), paisId, paisNombre);
    }
}