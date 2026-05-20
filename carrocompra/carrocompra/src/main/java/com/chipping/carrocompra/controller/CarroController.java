package com.chipping.carrocompra.controller;

import com.chipping.carrocompra.dto.CarroResponseDTO;
import com.chipping.carrocompra.dto.ItemCarroRequestDTO;
import com.chipping.carrocompra.service.CarroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carro")
@RequiredArgsConstructor
public class CarroController {

    private final CarroService carroService;

    // GET /carro/{usuarioId}  -> obtener o crear carro activo del usuario
    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarroResponseDTO> obtenerCarro(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carroService.obtenerOCrearCarro(usuarioId));
    }

    // POST /carro/{usuarioId}/items  -> agregar producto al carro
    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<CarroResponseDTO> agregarItem(
            @PathVariable Long usuarioId,
            @Valid @RequestBody ItemCarroRequestDTO request) {
        return ResponseEntity.status(201).body(carroService.agregarItem(usuarioId, request));
    }

    // DELETE /carro/{usuarioId}/items/{itemId}  -> quitar un item del carro
    @DeleteMapping("/{usuarioId}/items/{itemId}")
    public ResponseEntity<CarroResponseDTO> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(carroService.eliminarItem(usuarioId, itemId));
    }

    // DELETE /carro/{usuarioId}/vaciar  -> vaciar todo el carro
    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarro(@PathVariable Long usuarioId) {
        carroService.vaciarCarro(usuarioId);
        return ResponseEntity.noContent().build();
    }
}