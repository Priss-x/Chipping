package com.chipping.inventario.controller;

import com.chipping.inventario.dto.InventarioRequestDTO;
import com.chipping.inventario.dto.StockResponseDTO;
import com.chipping.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/{productoId}")
    public ResponseEntity<StockResponseDTO> obtenerStock(@PathVariable Long productoId) {
        return inventarioService.obtenerStock(productoId)
                .map(inventario -> {
                    String estadoDinamico = (inventario.getCantidad() <= 0) ? "AGOTADO" : "DISPONIBLE";

                    return ResponseEntity.ok(new StockResponseDTO(
                            inventario.getProductoId(),
                            inventario.getCantidad(),
                            estadoDinamico
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/descontar")
    public ResponseEntity<StockResponseDTO> descontar(@RequestBody InventarioRequestDTO request) {
        boolean exito = inventarioService.descontarStock(
                request.getProductoId(),
                request.getCantidad()
        );

        if (exito) {
            return inventarioService.obtenerStock(request.getProductoId())
                    .map(inventario -> {
                        String estadoDinamico = (inventario.getCantidad() <= 0) ? "AGOTADO" : "DISPONIBLE";
                        return ResponseEntity.ok(new StockResponseDTO(
                                inventario.getProductoId(),
                                inventario.getCantidad(),
                                estadoDinamico
                        ));
                    })
                    .orElse(ResponseEntity.badRequest().build());
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/reponer")
    public ResponseEntity<StockResponseDTO> reponer(@RequestBody InventarioRequestDTO request) {
        boolean exito = inventarioService.reponerStock(
                request.getProductoId(),
                request.getCantidad()
        );

        if (exito) {
            return inventarioService.obtenerStock(request.getProductoId())
                    .map(inventario -> ResponseEntity.ok(new StockResponseDTO(
                            inventario.getProductoId(),
                            inventario.getCantidad(),
                            "DISPONIBLE"
                    )))
                    .orElse(ResponseEntity.badRequest().build());
        }
        return ResponseEntity.badRequest().build();
    }
}