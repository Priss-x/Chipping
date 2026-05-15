package com.chipping.inventario.controller;

import com.chipping.inventario.entity.Inventario;
import com.chipping.inventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/producto/{id}")
    public ResponseEntity<Inventario> verStock(@PathVariable Long id){
        return inventarioService.obtenerStock(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/descontar")
    public ResponseEntity<String> descontar(@RequestParam Long id, @RequestParam Integer cantidad){
        boolean exito = inventarioService.descontarStock(id, cantidad);
        if(exito){
            return ResponseEntity.ok("Stock actualizado correctamente");
        }
        return ResponseEntity.badRequest().body("No hay stock suficiente o el producto no existe");
    }
}
