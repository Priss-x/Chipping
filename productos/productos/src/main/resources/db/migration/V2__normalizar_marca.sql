CREATE TABLE marcas (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO marcas (nombre)
SELECT DISTINCT marca FROM productos WHERE marca IS NOT NULL AND marca <> '';

ALTER TABLE productos ADD COLUMN marca_id BIGINT;

UPDATE productos p
    JOIN marcas m ON p.marca = m.nombre
    SET p.marca_id = m.id;

ALTER TABLE productos
    ADD CONSTRAINT fk_producto_marca FOREIGN KEY (marca_id) REFERENCES marcas(id);

ALTER TABLE productos DROP COLUMN marca;