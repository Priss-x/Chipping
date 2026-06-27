CREATE TABLE estados_pedido (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                nombre VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO estados_pedido (nombre) VALUES ('PENDIENTE');
INSERT INTO estados_pedido (nombre) VALUES ('PAGADO');
INSERT INTO estados_pedido (nombre) VALUES ('CANCELADO');
INSERT INTO estados_pedido (nombre) VALUES ('ENVIADO');
INSERT INTO estados_pedido (nombre) VALUES ('ENTREGADO');

INSERT INTO estados_pedido (nombre)
SELECT DISTINCT estado FROM pedidos
WHERE estado IS NOT NULL AND estado NOT IN (SELECT nombre FROM estados_pedido);

ALTER TABLE pedidos ADD COLUMN estado_id BIGINT;

UPDATE pedidos p
    JOIN estados_pedido e ON p.estado = e.nombre
    SET p.estado_id = e.id;

ALTER TABLE pedidos MODIFY COLUMN estado_id BIGINT NOT NULL;
ALTER TABLE pedidos
    ADD CONSTRAINT fk_pedido_estado FOREIGN KEY (estado_id) REFERENCES estados_pedido(id);

ALTER TABLE pedidos DROP COLUMN estado;