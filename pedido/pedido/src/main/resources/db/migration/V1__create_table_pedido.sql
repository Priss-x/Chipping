DROP TABLE IF EXISTS items_pedido;
DROP TABLE IF EXISTS pedidos;

CREATE TABLE pedidos (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         usuario_id BIGINT NOT NULL,
                         carro_id BIGINT NOT NULL,
                         estado VARCHAR(30) NOT NULL,
                         total INT NOT NULL,
                         direccion_envio VARCHAR(500) NOT NULL,
                         fecha_creacion DATETIME NOT NULL,
                         fecha_actualizacion DATETIME NOT NULL
);

CREATE TABLE items_pedido (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              pedido_id BIGINT NOT NULL,
                              producto_id BIGINT NOT NULL,
                              nombre_producto VARCHAR(500) NOT NULL,
                              cantidad INT NOT NULL,
                              precio_unitario INT NOT NULL,
                              subtotal INT NOT NULL,
                              CONSTRAINT fk_pedido
                                  FOREIGN KEY (pedido_id)
                                      REFERENCES pedidos(id)
                                      ON DELETE CASCADE
);