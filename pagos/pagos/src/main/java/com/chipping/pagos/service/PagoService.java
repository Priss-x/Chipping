package com.chipping.pagos.service;

import com.chipping.pagos.client.CarroClient;
import com.chipping.pagos.client.InventarioClient;
import com.chipping.pagos.client.PedidoClient;
import com.chipping.pagos.dto.PagoRequestDTO;
import com.chipping.pagos.dto.PedidoDTO;
import com.chipping.pagos.dto.TransaccionResponseDTO;
import com.chipping.pagos.model.EstadoPago;
import com.chipping.pagos.model.MetodoPago;
import com.chipping.pagos.model.Transaccion;
import com.chipping.pagos.repository.EstadoPagoRepository;
import com.chipping.pagos.repository.MetodoPagoRepository;
import com.chipping.pagos.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService {

    private final TransaccionRepository transaccionRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final EstadoPagoRepository estadoPagoRepository;
    private final PedidoClient pedidoClient;
    private final InventarioClient inventarioClient;
    private final CarroClient carroClient;

    public TransaccionResponseDTO procesarPago(PagoRequestDTO request) {
        if (transaccionRepository.findByPedidoId(request.getPedidoId()).isPresent()) {
            throw new RuntimeException("Ya existe una transacción para el pedido "
                    + request.getPedidoId());
        }

        PedidoDTO pedido = pedidoClient.obtenerPedido(request.getPedidoId());
        if (pedido == null) {
            throw new RuntimeException("El pedido " + request.getPedidoId() + " no existe");
        }
        if (!pedido.getEstado().equals("PENDIENTE")) {
            throw new RuntimeException("El pedido no está en estado PENDIENTE");
        }
        if (!request.getMonto().equals(pedido.getTotal())) {
            throw new RuntimeException("El monto ingresado " + request.getMonto()
                    + " no coincide con el total del pedido " + pedido.getTotal());
        }

        MetodoPago metodoPago = metodoPagoRepository.findByNombre(request.getMetodoPago())
                .orElseThrow(() -> new RuntimeException("El método de pago '" + request.getMetodoPago() + "' no es válido"));

        boolean aprobado = request.getMonto() > 0;
        EstadoPago estado = estadoPagoRepository.findByNombre(aprobado ? "APROBADO" : "RECHAZADO")
                .orElseThrow(() -> new RuntimeException("El estado de pago no existe en el catálogo"));
        String codigoAutorizacion = aprobado
                ? UUID.randomUUID().toString().substring(0, 8).toUpperCase()
                : null;

        Transaccion transaccion = new Transaccion(
                null,
                request.getPedidoId(),
                request.getUsuarioId(),
                request.getMonto(),
                metodoPago,
                estado,
                codigoAutorizacion,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Transaccion guardada = transaccionRepository.save(transaccion);

        if (aprobado) {
            pedidoClient.actualizarEstado(request.getPedidoId(), "PAGADO");

            if (pedido.getItems() != null) {
                pedido.getItems().forEach(item -> {
                    try {
                        inventarioClient.descontarStock(
                                item.getProductoId(),
                                item.getCantidad()
                        );
                        System.out.println("Stock descontado: producto "
                                + item.getProductoId()
                                + " cantidad " + item.getCantidad());
                    } catch (Exception e) {
                        System.err.println("Error al descontar stock: " + e.getMessage());
                    }
                });
            }

            try {
                carroClient.vaciarCarro(request.getUsuarioId());
            } catch (Exception e) {
                System.err.println("Error al vaciar carro: " + e.getMessage());
            }

        } else {
            pedidoClient.actualizarEstado(request.getPedidoId(), "CANCELADO");
        }

        return mapToDTO(guardada);
    }

    public Optional<TransaccionResponseDTO> obtenerPorId(Long id) {
        return transaccionRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<TransaccionResponseDTO> obtenerPorPedido(Long pedidoId) {
        return transaccionRepository.findByPedidoId(pedidoId).map(this::mapToDTO);
    }

    public List<TransaccionResponseDTO> obtenerPorUsuario(Long usuarioId) {
        return transaccionRepository.findByUsuarioId(usuarioId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<TransaccionResponseDTO> obtenerTodas() {
        return transaccionRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private TransaccionResponseDTO mapToDTO(Transaccion t) {
        return new TransaccionResponseDTO(
                t.getId(), t.getPedidoId(), t.getUsuarioId(),
                t.getMonto(), t.getMetodoPago().getNombre(), t.getEstado().getNombre(),
                t.getCodigoAutorizacion(), t.getFechaCreacion());
    }
}