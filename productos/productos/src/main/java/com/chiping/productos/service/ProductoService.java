package com.chiping.productos.service;

import com.chiping.productos.client.ProveedorClient;
import com.chiping.productos.dto.ProductoRequestDTO;
import com.chiping.productos.dto.ProductoResponseDTO;
import com.chiping.productos.dto.ProveedorDTO;
import com.chiping.productos.model.Categoria;
import com.chiping.productos.model.Marca;
import com.chiping.productos.model.Producto;
import com.chiping.productos.repository.CategoriaRepository;
import com.chiping.productos.repository.MarcaRepository;
import com.chiping.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProveedorClient proveedorClient;

    private ProductoResponseDTO mapProducto(Producto p) {
        ProveedorDTO provDTO = null;
        try {
            provDTO = proveedorClient.obtenerProveedor(p.getProveedorId());
        } catch (Exception e) {
            System.err.println("No se pudo obtener el proveedor para el ID: " + p.getProveedorId());
        }

        Long marcaId = (p.getMarca() != null) ? p.getMarca().getId() : null;
        String marcaNombre = (p.getMarca() != null) ? p.getMarca().getNombre() : "Sin marca";

        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                marcaId,
                marcaNombre,
                p.getDescripcionCorta(),
                (provDTO != null) ? provDTO.getNombre() : "N/A",
                (p.getCategoria() != null) ? p.getCategoria().getDescripcion() : "Sin categoria"
        );
    }
    public List<ProductoResponseDTO> obtenerTodos()
    {
        return productoRepository.findAll()
                .stream()
                .map(this::mapProducto)
                .collect(Collectors.toList());
    }
    public Optional<ProductoResponseDTO> obtenerPorId(Long id)
    {
        return productoRepository.findById(id).map(this::mapProducto);
    }
    public ProductoResponseDTO guardar(ProductoRequestDTO productoDTO) {
        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no existe"));

        Marca marca = marcaRepository.findById(productoDTO.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no existe"));

        Producto producto = new Producto(
                null,
                productoDTO.getNombre(),
                productoDTO.getPrecio(),
                productoDTO.getStock(),
                productoDTO.getDescripcionCorta(),
                productoDTO.getProveedorId(),
                categoria,
                marca
        );
        return mapProducto(productoRepository.save(producto));
    }

    public void eliminar(Long id)
    {
        productoRepository.deleteById(id);
    }

    public List<ProductoResponseDTO> buscaPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapProducto)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> buscaPorPrecio(Integer min, Integer max) {
        return productoRepository.findByPrecioBetween(min, max).stream()
                .map(this::mapProducto)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> buscaPorCategoriaDesc(String categoria) {
        return productoRepository.findByCategoriaDesc(categoria).stream()
                .map(this::mapProducto)
                .collect(Collectors.toList());
    }

}