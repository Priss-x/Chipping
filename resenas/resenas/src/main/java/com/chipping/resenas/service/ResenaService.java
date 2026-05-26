package com.chipping.resenas.service;

import com.chipping.resenas.client.ProductoClient;
import com.chipping.resenas.dto.ProductoDTO;
import com.chipping.resenas.dto.ResenaRequestDTO;
import com.chipping.resenas.dto.ResenaResponseDTO;
import com.chipping.resenas.model.Resena;
import com.chipping.resenas.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final ProductoClient productoClient;

    public ResenaResponseDTO crearResena(ResenaRequestDTO request) {
        ProductoDTO producto = productoClient.obtenerProducto(request.getProductoId());
        if (producto == null) {
            throw new RuntimeException("El producto "
                    + request.getProductoId() + " no existe");
        }

        if (resenaRepository.findByUsuarioIdAndProductoId(
                request.getUsuarioId(), request.getProductoId()).isPresent()) {
            throw new RuntimeException("El usuario ya realizó una reseña "
                    + "para el producto " + request.getProductoId());
        }

        Resena resena = new Resena(
                null,
                request.getUsuarioId(),
                request.getProductoId(),
                request.getCalificacion(),
                request.getComentario(),
                producto.getNombre(),
                LocalDateTime.now()
        );

        return mapToDTO(resenaRepository.save(resena));
    }

    public List<ResenaResponseDTO> obtenerPorProducto(Long productoId) {
        return resenaRepository.findByProductoId(productoId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ResenaResponseDTO> obtenerPorUsuario(Long usuarioId) {
        return resenaRepository.findByUsuarioId(usuarioId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ResenaResponseDTO> obtenerTodas() {
        return resenaRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<ResenaResponseDTO> obtenerPorId(Long id) {
        return resenaRepository.findById(id).map(this::mapToDTO);
    }

    public void eliminar(Long id) {
        resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        resenaRepository.deleteById(id);
    }

    private ResenaResponseDTO mapToDTO(Resena r) {
        return new ResenaResponseDTO(
                r.getId(),
                r.getUsuarioId(),
                r.getProductoId(),
                r.getNombreProducto(),
                r.getCalificacion(),
                r.getComentario(),
                r.getFechaCreacion());
    }
}