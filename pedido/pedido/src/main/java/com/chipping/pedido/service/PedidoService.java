package com.chipping.pedido.service;

import com.chipping.pedido.client.CarroClient;
import com.chipping.pedido.dto.CarroDTO;
import com.chipping.pedido.dto.ItemPedidoDTO;
import com.chipping.pedido.dto.PedidoRequestDTO;
import com.chipping.pedido.dto.PedidoResponseDTO;
import com.chipping.pedido.model.ItemPedido;
import com.chipping.pedido.model.Pedido;
import com.chipping.pedido.repository.PedidoRepository;
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
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarroClient carroClient;

    public PedidoResponseDTO crearPedido(PedidoRequestDTO request) {
        CarroDTO carro = carroClient.obtenerCarro(request.getUsuarioId());

        if (carro == null || carro.getItems() == null || carro.getItems().isEmpty()) {
            throw new RuntimeException("El carro está vacío, no se puede crear un pedido");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(request.getUsuarioId());
        pedido.setCarroId(carro.getId());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(carro.getTotal());
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setFechaActualizacion(LocalDateTime.now());

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        List<ItemPedido> items = carro.getItems().stream().map(itemDTO -> {
            ItemPedido item = new ItemPedido();
            item.setPedido(pedidoGuardado);
            item.setProductoId(itemDTO.getProductoId());
            item.setNombreProducto(itemDTO.getNombreProducto());
            item.setCantidad(itemDTO.getCantidad());
            item.setPrecioUnitario(itemDTO.getPrecioUnitario());
            item.setSubtotal(itemDTO.getSubtotal());
            return item;
        }).collect(Collectors.toList());

        pedidoGuardado.setItems(items);
        pedidoRepository.save(pedidoGuardado);

        return mapToDTO(pedidoGuardado);
    }

    public Optional<PedidoResponseDTO> obtenerPorId(Long id) {
        return pedidoRepository.findById(id).map(this::mapToDTO);
    }

    public List<PedidoResponseDTO> obtenerPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> obtenerTodos() {
        return pedidoRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public PedidoResponseDTO actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        pedido.setFechaActualizacion(LocalDateTime.now());
        return mapToDTO(pedidoRepository.save(pedido));
    }

    private PedidoResponseDTO mapToDTO(Pedido pedido) {
        List<ItemPedidoDTO> items = pedido.getItems().stream()
                .map(i -> new ItemPedidoDTO(
                        i.getProductoId(),
                        i.getNombreProducto(),
                        i.getCantidad(),
                        i.getPrecioUnitario(),
                        i.getSubtotal()))
                .collect(Collectors.toList());

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getUsuarioId(),
                pedido.getCarroId(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getDireccionEnvio(),
                pedido.getFechaCreacion(),
                items);
    }

    public void eliminar(Long id) {
        pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedidoRepository.deleteById(id);
    }
}