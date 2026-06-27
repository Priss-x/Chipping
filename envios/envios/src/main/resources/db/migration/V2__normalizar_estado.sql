CREATE TABLE estados_envio (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               nombre VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO estados_envio (nombre) VALUES ('PREPARANDO');
INSERT INTO estados_envio (nombre) VALUES ('EN_CAMINO');
INSERT INTO estados_envio (nombre) VALUES ('ENTREGADO');
INSERT INTO estados_envio (nombre) VALUES ('CANCELADO');

INSERT INTO estados_envio (nombre)
SELECT DISTINCT estado FROM envios
WHERE estado IS NOT NULL AND estado NOT IN (SELECT nombre FROM estados_envio);

ALTER TABLE envios ADD COLUMN estado_id BIGINT;

UPDATE envios e
    JOIN estados_envio es ON e.estado = es.nombre
    SET e.estado_id = es.id;

ALTER TABLE envios MODIFY COLUMN estado_id BIGINT NOT NULL;
ALTER TABLE envios
    ADD CONSTRAINT fk_envio_estado FOREIGN KEY (estado_id) REFERENCES estados_envio(id);

ALTER TABLE envios DROP COLUMN estado;