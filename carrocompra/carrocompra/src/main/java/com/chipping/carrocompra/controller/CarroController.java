package com.chipping.carrocompra.controller;

import com.chipping.carrocompra.dto.CarroResponseDTO;
import com.chipping.carrocompra.dto.ItemCarroRequestDTO;
import com.chipping.carrocompra.service.CarroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carro")
@RequiredArgsConstructor
public class CarroController {

    @Autowired
    private final CarroService carroService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarroResponseDTO> obtenerCarro(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carroService.obtenerOCrearCarro(usuarioId));
    }

    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<CarroResponseDTO> agregarItem(
            @PathVariable Long usuarioId,
            @Valid @RequestBody ItemCarroRequestDTO request) {
        CarroResponseDTO response = carroService.agregarItem(usuarioId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{usuarioId}/items/{itemId}")
    public ResponseEntity<CarroResponseDTO> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(carroService.eliminarItem(usuarioId, itemId));
    }
    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarro(@PathVariable Long usuarioId) {
        carroService.vaciarCarro(usuarioId);
        return ResponseEntity.noContent().build();
    }
}