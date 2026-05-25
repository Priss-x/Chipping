package com.chipping.carrocompra.service;

import com.chipping.carrocompra.client.InventarioClient;
import com.chipping.carrocompra.client.ProductoClient;
import com.chipping.carrocompra.dto.CarroResponseDTO;
import com.chipping.carrocompra.dto.ItemCarroRequestDTO;
import com.chipping.carrocompra.dto.ItemCarroResponseDTO;
import com.chipping.carrocompra.dto.ProductoDTO;
import com.chipping.carrocompra.model.Carro;
import com.chipping.carrocompra.model.ItemCarro;
import com.chipping.carrocompra.repository.CarroRepository;
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
    private final ProductoClient productoClient;
    private final InventarioClient inventarioClient;

    public CarroResponseDTO obtenerOCrearCarro(Long usuarioId) {

        Carro carro = carroRepository
                .findByUsuarioIdAndEstado(usuarioId, "ACTIVO")
                .orElseGet(() -> {

                    Carro nuevo = new Carro(
                            null,
                            usuarioId,
                            "ACTIVO",
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            new ArrayList<>()
                    );

                    return carroRepository.save(nuevo);
                });

        return mapToDTO(carro);
    }

    public CarroResponseDTO agregarItem(Long usuarioId,
                                        ItemCarroRequestDTO request) {

        ProductoDTO producto =
                productoClient.obtenerProducto(request.getProductoId());

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        Integer stock =
                inventarioClient.obtenerStock(request.getProductoId());

        if (stock == null) {
            throw new RuntimeException("No se pudo obtener stock");
        }

        if (stock < request.getCantidad()) {
            throw new RuntimeException(
                    "No hay suficiente stock. Disponible: " + stock
            );
        }

        Carro carro = carroRepository
                .findByUsuarioIdAndEstado(usuarioId, "ACTIVO")
                .orElseGet(() -> {

                    Carro nuevo = new Carro(
                            null,
                            usuarioId,
                            "ACTIVO",
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            new ArrayList<>()
                    );

                    return carroRepository.save(nuevo);
                });

        Optional<ItemCarro> itemExistente =
                itemCarroRepository.findByCarroIdAndProductoId(
                        carro.getId(),
                        request.getProductoId()
                );

        if (itemExistente.isPresent()) {

            ItemCarro item = itemExistente.get();

            item.setCantidad(
                    item.getCantidad() + request.getCantidad()
            );

            itemCarroRepository.save(item);

        } else {

            ItemCarro nuevoItem = new ItemCarro(
                    null,
                    carro,
                    request.getProductoId(),
                    request.getCantidad(),
                    producto.getPrecio()
            );

            carro.getItems().add(nuevoItem);

            itemCarroRepository.save(nuevoItem);
        }

        carro.setFechaActualizacion(LocalDateTime.now());

        carroRepository.save(carro);

        Carro carroActualizado = carroRepository
                .findById(carro.getId())
                .orElseThrow(() ->
                        new RuntimeException("Carro no encontrado")
                );

        return mapToDTO(carroActualizado);
    }

    public CarroResponseDTO eliminarItem(Long usuarioId, Long itemId) {

        Carro carro = carroRepository
                .findByUsuarioIdAndEstado(usuarioId, "ACTIVO")
                .orElseThrow(() ->
                        new RuntimeException(
                                "No hay carro activo para el usuario "
                                        + usuarioId
                        )
                );

        itemCarroRepository.deleteById(itemId);

        carro.setFechaActualizacion(LocalDateTime.now());

        carroRepository.save(carro);

        Carro carroActualizado = carroRepository
                .findById(carro.getId())
                .orElseThrow(() ->
                        new RuntimeException("Carro no encontrado")
                );

        return mapToDTO(carroActualizado);
    }

    public void vaciarCarro(Long usuarioId) {

        Carro carro = carroRepository
                .findByUsuarioIdAndEstado(usuarioId, "ACTIVO")
                .orElseThrow(() ->
                        new RuntimeException(
                                "No hay carro activo para el usuario "
                                        + usuarioId
                        )
                );

        carro.getItems().clear();

        carro.setFechaActualizacion(LocalDateTime.now());

        carroRepository.save(carro);
    }

    public void cerrarCarro(Long carroId) {

        Carro carro = carroRepository
                .findById(carroId)
                .orElseThrow(() ->
                        new RuntimeException("Carro no encontrado")
                );

        carro.setEstado("CERRADO");

        carroRepository.save(carro);
    }

    private CarroResponseDTO mapToDTO(Carro carro) {

        List<ItemCarroResponseDTO> items =
                carro.getItems()
                        .stream()
                        .map(item -> {

                            String nombreProducto = "N/A";

                            try {

                                ProductoDTO producto =
                                        productoClient.obtenerProducto(
                                                item.getProductoId()
                                        );

                                if (producto != null) {
                                    nombreProducto =
                                            producto.getNombre();
                                }

                            } catch (Exception ignored) {
                            }

                            return new ItemCarroResponseDTO(
                                    item.getId(),
                                    item.getProductoId(),
                                    nombreProducto,
                                    item.getCantidad(),
                                    item.getPrecioUnitario(),
                                    item.getCantidad()
                                            * item.getPrecioUnitario()
                            );

                        }).collect(Collectors.toList());

        int total = items.stream()
                .mapToInt(ItemCarroResponseDTO::getSubtotal)
                .sum();

        return new CarroResponseDTO(
                carro.getId(),
                carro.getUsuarioId(),
                carro.getEstado(),
                carro.getFechaCreacion(),
                items,
                total
        );
    }
}