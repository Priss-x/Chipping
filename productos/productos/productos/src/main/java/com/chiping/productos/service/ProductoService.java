package com.chiping.productos.service;

import com.chiping.productos.client.ProveedorClient;
import com.chiping.productos.dto.ProductoRequestDTO;
import com.chiping.productos.dto.ProductoResponseDTO;
import com.chiping.productos.dto.ProveedorDTO;
import com.chiping.productos.model.Categoria;
import com.chiping.productos.model.Producto;
import com.chiping.productos.repository.CategoriaRepository;
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
    private final ProveedorClient proveedorClient;

    private ProductoResponseDTO mapProducto(Producto p) {
        ProveedorDTO provDTO = null;
        try {
            provDTO = proveedorClient.obtenerProveedor(p.getProveedorId());
        } catch (Exception e) {
            System.err.println("No se pudo obtener el proveedor para el ID: " + p.getProveedorId());
        }

        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getMarca(),
                p.getDescripcion_corta(),
                (provDTO != null) ? provDTO.getNombre() : "N/A",
                (p.getCategoria() != null) ? String.valueOf(p.getCategoria().getId()) : "0"
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

        Producto producto = new Producto(
                null,
                productoDTO.getNombre(),
                productoDTO.getPrecio(),
                productoDTO.getStock(),
                productoDTO.getMarca(),
                productoDTO.getDescripcionCorta(),
                productoDTO.getProveedorId(),
                categoria
        );
        return mapProducto(productoRepository.save(producto));

    }

    public void eliminar(Long id)
    {
        productoRepository.deleteById(id);
    }

    public List<Producto> buscaPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscaPorPrecio(Integer min, Integer max) {
        return productoRepository.findByPrecioBetween(min, max);
    }

    public List<Producto> buscaPorCategoriaDesc(String categoria) {
        return productoRepository.findByCategoriaDesc(categoria);
    }


}
