DROP TABLE IF EXISTS transacciones;

CREATE TABLE transacciones (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               pedido_id BIGINT NOT NULL UNIQUE,
                               usuario_id BIGINT NOT NULL,
                               monto INT NOT NULL,
                               metodo_pago VARCHAR(50) NOT NULL,
                               estado VARCHAR(30) NOT NULL,
                               codigo_autorizacion VARCHAR(100),
                               fecha_creacion DATETIME NOT NULL,
                               fecha_actualizacion DATETIME NOT NULL
);