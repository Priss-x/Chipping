CREATE TABLE inventario (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   producto_id BIGINT NOT NULL UNIQUE,
   cantidad_disponible INT NOT NULL DEFAULT 0
);