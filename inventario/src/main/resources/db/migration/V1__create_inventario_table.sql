CREATE TABLE inventario (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   producto_id BIGINT NOT NULL UNIQUE,
   cantidad_disponible INT NOT NULL DEFAULT 0
);

INSERT INTO inventario (producto_id, cantidad_disponible)
VALUES (1, 8);

INSERT INTO inventario (producto_id, cantidad_disponible)
VALUES (2, 8);

INSERT INTO inventario (producto_id, cantidad_disponible)
VALUES (3, 5);

INSERT INTO inventario (producto_id, cantidad_disponible)
VALUES (4, 7);

INSERT INTO inventario (producto_id, cantidad_disponible)
VALUES (5, 15);