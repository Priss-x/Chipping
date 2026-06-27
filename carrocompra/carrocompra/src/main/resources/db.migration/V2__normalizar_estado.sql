CREATE TABLE estados_carro (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               nombre VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO estados_carro (nombre) VALUES ('ACTIVO');

INSERT INTO estados_carro (nombre)
SELECT DISTINCT estado FROM carros
WHERE estado IS NOT NULL AND estado NOT IN (SELECT nombre FROM estados_carro);

ALTER TABLE carros ADD COLUMN estado_id BIGINT;

UPDATE carros c
    JOIN estados_carro e ON c.estado = e.nombre
    SET c.estado_id = e.id;

ALTER TABLE carros MODIFY COLUMN estado_id BIGINT NOT NULL;
ALTER TABLE carros
    ADD CONSTRAINT fk_carro_estado FOREIGN KEY (estado_id) REFERENCES estados_carro(id);

ALTER TABLE carros DROP COLUMN estado;