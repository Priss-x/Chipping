package com.chipping.pagos.controller;

import com.chipping.pagos.dto.PagoRequestDTO;
import com.chipping.pagos.dto.TransaccionResponseDTO;
import com.chipping.pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> procesarPago(
            @Valid @RequestBody PagoRequestDTO request) {
        return ResponseEntity.status(201).body(pagoService.procesarPago(request));
    }

    @GetMapping
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(pagoService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<TransaccionResponseDTO> obtenerPorPedido(
            @PathVariable Long pedidoId) {
        return pagoService.obtenerPorPedido(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerPorUsuario(
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.obtenerPorUsuario(usuarioId));
    }
}