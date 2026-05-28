package com.chiping.productos.controller;

import com.chiping.productos.dto.ProductoRequestDTO;
import com.chiping.productos.dto.ProductoResponseDTO;
import com.chiping.productos.model.Producto;
import com.chiping.productos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodos()
    {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO producto)
    {
        return ResponseEntity.status(201).body(productoService.guardar(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id)
    {
        if(productoService.obtenerPorId(id).isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Producto>> buscaPorNombre(@RequestParam String nombre)
    {
        return ResponseEntity.ok(productoService.buscaPorNombre(nombre));
    }

    @GetMapping("/buscar/precio")
    public ResponseEntity<List<Producto>> buscarPorPrecios(@RequestParam Integer min, @RequestParam Integer max)
    {
        return ResponseEntity.ok(productoService.buscaPorPrecio(min,max));
    }

    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<Producto>> buscarPorCategoriaDesc(@RequestParam String categoria)
    {
        return ResponseEntity.ok(productoService.buscaPorCategoriaDesc(categoria));
    }
}




