DROP TABLE IF EXISTS envios;

CREATE TABLE envios (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        pedido_id BIGINT NOT NULL UNIQUE,
                        usuario_id BIGINT NOT NULL,
                        direccion_destino VARCHAR(500) NOT NULL,
                        estado VARCHAR(30) NOT NULL,
                        numero_seguimiento VARCHAR(100),
                        fecha_estimada DATE,
                        fecha_creacion DATETIME NOT NULL,
                        fecha_actualizacion DATETIME NOT NULL
);