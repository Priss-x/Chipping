package com.chipping.envios.service;

import com.chipping.envios.client.PedidoClient;
import com.chipping.envios.dto.EnvioRequestDTO;
import com.chipping.envios.dto.EnvioResponseDTO;
import com.chipping.envios.model.EstadoEnvio;
import com.chipping.envios.model.Envio;
import com.chipping.envios.repository.EnvioRepository;
import com.chipping.envios.repository.EstadoEnvioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final EstadoEnvioRepository estadoEnvioRepository;
    private final PedidoClient pedidoClient;

    private EstadoEnvio obtenerEstadoPorNombre(String nombre) {
        return estadoEnvioRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("El estado '" + nombre + "' no existe en estados_envio"));
    }

    public EnvioResponseDTO crearEnvio(EnvioRequestDTO request) {
        if (envioRepository.findByPedidoId(request.getPedidoId()).isPresent()) {
            throw new RuntimeException("Ya existe un envio para el pedido "
                    + request.getPedidoId());
        }

        String numeroSeguimiento = "CHIP-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Envio envio = new Envio(
                null,
                request.getPedidoId(),
                request.getUsuarioId(),
                request.getDireccionDestino(),
                obtenerEstadoPorNombre("PREPARANDO"),
                numeroSeguimiento,
                LocalDate.now().plusDays(5),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return mapToDTO(envioRepository.save(envio));
    }

    public EnvioResponseDTO actualizarEstado(Long id, String nuevoEstado) {
        nuevoEstado = nuevoEstado.trim();
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado"));

        EstadoEnvio estado = obtenerEstadoPorNombre(nuevoEstado);
        envio.setEstado(estado);
        envio.setFechaActualizacion(LocalDateTime.now());
        envioRepository.save(envio);

        try {
            if ("EN_CAMINO".equals(nuevoEstado)) {
                pedidoClient.actualizarEstado(envio.getPedidoId(), "ENVIADO");
            } else if ("ENTREGADO".equals(nuevoEstado)) {
                pedidoClient.actualizarEstado(envio.getPedidoId(), "ENTREGADO");
            }
        } catch (Exception e) {
            System.err.println("No se pudo notificar a pedido: " + e.getMessage());
        }

        return mapToDTO(envio);
    }

    public Optional<EnvioResponseDTO> obtenerPorId(Long id) {
        return envioRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<EnvioResponseDTO> obtenerPorPedido(Long pedidoId) {
        return envioRepository.findByPedidoId(pedidoId).map(this::mapToDTO);
    }

    public List<EnvioResponseDTO> obtenerPorUsuario(Long usuarioId) {
        return envioRepository.findByUsuarioId(usuarioId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<EnvioResponseDTO> obtenerTodos() {
        return envioRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private EnvioResponseDTO mapToDTO(Envio envio) {
        return new EnvioResponseDTO(
                envio.getId(),
                envio.getPedidoId(),
                envio.getUsuarioId(),
                envio.getDireccionDestino(),
                envio.getEstado().getNombre(),
                envio.getNumeroSeguimiento(),
                envio.getFechaEstimada(),
                envio.getFechaCreacion());
    }
}