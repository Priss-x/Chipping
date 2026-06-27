package com.chipping.carrocompra.service;

import com.chipping.carrocompra.client.InventarioClient;
import com.chipping.carrocompra.client.ProductoClient;
import com.chipping.carrocompra.dto.*;
import com.chipping.carrocompra.model.Carro;
import com.chipping.carrocompra.model.EstadoCarro;
import com.chipping.carrocompra.model.ItemCarro;
import com.chipping.carrocompra.repository.CarroRepository;
import com.chipping.carrocompra.repository.EstadoCarroRepository;
import com.chipping.carrocompra.repository.ItemCarroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CarroService {

    private final CarroRepository carroRepository;
    private final ItemCarroRepository itemCarroRepository;
    private final EstadoCarroRepository estadoCarroRepository;
    private final ProductoClient productoClient;
    private final InventarioClient inventarioClient;

    private EstadoCarro obtenerEstadoActivo() {
        return estadoCarroRepository.findByNombre("ACTIVO")
                .orElseThrow(() -> new RuntimeException("El estado 'ACTIVO' no existe en estados_carro"));
    }

    public CarroResponseDTO obtenerOCrearCarro(Long usuarioId) {
        Carro carro = carroRepository
                .findByUsuarioIdAndEstadoNombre(usuarioId, "ACTIVO")
                .orElseGet(() -> carroRepository.save(
                        new Carro(null, usuarioId, obtenerEstadoActivo(), LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>())
                ));
        return mapToDTO(carro);
    }

    public CarroResponseDTO agregarItem(Long usuarioId, ItemCarroRequestDTO request) {
        ProductoDTO producto = productoClient.obtenerProducto(request.getProductoId());
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado en el catálogo");
        }
        Carro carro = carroRepository
                .findByUsuarioIdAndEstadoNombre(usuarioId, "ACTIVO")
                .orElseGet(() -> carroRepository.save(
                        new Carro(null, usuarioId, obtenerEstadoActivo(), LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>())
                ));

        Optional<ItemCarro> itemExistente = itemCarroRepository
                .findByCarroIdAndProductoId(carro.getId(), request.getProductoId());

        boolean descuentoExitoso = inventarioClient.descontarStock(request.getProductoId(), request.getCantidad());
        if (!descuentoExitoso) {
            throw new RuntimeException("No se pudo descontar el stock. Stock insuficiente en la bodega.");
        }

        if (itemExistente.isPresent()) {
            ItemCarro item = itemExistente.get();
            item.setCantidad(item.getCantidad() + request.getCantidad());
            itemCarroRepository.save(item);
        } else {
            ItemCarro nuevoItem = new ItemCarro(
                    null,
                    carro,
                    request.getProductoId(),
                    request.getCantidad(),
                    producto.getPrecio()
            );
            itemCarroRepository.save(nuevoItem);
        }

        carro.setFechaActualizacion(LocalDateTime.now());
        carroRepository.save(carro);

        CarroResponseDTO respuestaForzada = new CarroResponseDTO();
        respuestaForzada.setId(carro.getId());
        respuestaForzada.setUsuarioId(carro.getUsuarioId());
        respuestaForzada.setEstado(carro.getEstado().getNombre());
        respuestaForzada.setFechaCreacion(carro.getFechaCreacion());
        respuestaForzada.setItems(new ArrayList<>());
        respuestaForzada.setTotal(0);

        return respuestaForzada;
    }

    public CarroResponseDTO eliminarItem(Long usuarioId, Long itemId) {
        ItemCarro item = itemCarroRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("El ítem no existe en la base de datos"));
        try {
            inventarioClient.reponerStock(item.getProductoId(), item.getCantidad());
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo reponer stock en Inventario, pero continuamos: " + e.getMessage());
        }
        itemCarroRepository.delete(item);

        carroRepository.findByUsuarioIdAndEstadoNombre(usuarioId, "ACTIVO")
                .ifPresent(carro -> {
                    carro.getItems().remove(item);
                    carro.setFechaActualizacion(LocalDateTime.now());
                    carroRepository.save(carro);
                });
        return obtenerOCrearCarro(usuarioId);
    }

    public void vaciarCarro(Long usuarioId) {
        Carro carro = carroRepository
                .findByUsuarioIdAndEstadoNombre(usuarioId, "ACTIVO")
                .orElseThrow(() -> new RuntimeException("Carro activo no existe"));

        for (ItemCarro item : carro.getItems()) {
            inventarioClient.reponerStock(item.getProductoId(), item.getCantidad());
        }

        carro.getItems().clear();
        carro.setFechaActualizacion(LocalDateTime.now());
        carroRepository.save(carro);
    }

    private CarroResponseDTO mapToDTO(Carro carro) {
        List<ItemCarroResponseDTO> itemsDTO = carro.getItems()
                .stream()
                .map(item -> {
                    String nombreProducto = "Producto Remoto N/A";
                    try {
                        ProductoDTO p = productoClient.obtenerProducto(item.getProductoId());
                        if (p != null) {
                            nombreProducto = p.getNombre();
                        }
                    } catch (Exception ignored) {}

                    return new ItemCarroResponseDTO(
                            item.getId(),
                            item.getProductoId(),
                            nombreProducto,
                            item.getCantidad(),
                            item.getPrecioUnitario(),
                            item.getCantidad() * item.getPrecioUnitario()
                    );
                })
                .collect(Collectors.toList());

        int totalCarro = itemsDTO.stream()
                .mapToInt(ItemCarroResponseDTO::getSubtotal)
                .sum();

        return new CarroResponseDTO(
                carro.getId(),
                carro.getUsuarioId(),
                carro.getEstado().getNombre(),
                carro.getFechaCreacion(),
                itemsDTO,
                totalCarro
        );
    }
}