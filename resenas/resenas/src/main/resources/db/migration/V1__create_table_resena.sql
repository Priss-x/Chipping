DROP TABLE IF EXISTS resenas;

CREATE TABLE resenas (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         usuario_id BIGINT NOT NULL,
                         producto_id BIGINT NOT NULL,
                         calificacion INT NOT NULL,
                         comentario VARCHAR(1000),
                         nombre_producto VARCHAR(500),
                         fecha_creacion DATETIME NOT NULL
);