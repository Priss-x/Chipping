package com.chiping.productos.repository;

import com.chiping.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByPrecioBetween(Integer min, Integer max);

    @Query(value = "SELECT p.* FROM productos p INNER JOIN categorias c ON p.categoria_id = c.id WHERE c.descripcion = :desc", nativeQuery = true)
    List<Producto> findByCategoriaDesc(@Param("desc") String desc);



}
