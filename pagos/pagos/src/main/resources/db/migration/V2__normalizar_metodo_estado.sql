CREATE TABLE metodos_pago (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO metodos_pago (nombre) VALUES ('DEBITO');
INSERT INTO metodos_pago (nombre) VALUES ('CREDITO');
INSERT INTO metodos_pago (nombre) VALUES ('TRANSFERENCIA');

INSERT INTO metodos_pago (nombre)
SELECT DISTINCT metodo_pago FROM transacciones
WHERE metodo_pago IS NOT NULL AND metodo_pago NOT IN (SELECT nombre FROM metodos_pago);

ALTER TABLE transacciones ADD COLUMN metodo_pago_id BIGINT;

UPDATE transacciones t
    JOIN metodos_pago m ON t.metodo_pago = m.nombre
    SET t.metodo_pago_id = m.id;

ALTER TABLE transacciones MODIFY COLUMN metodo_pago_id BIGINT NOT NULL;
ALTER TABLE transacciones
    ADD CONSTRAINT fk_transaccion_metodo FOREIGN KEY (metodo_pago_id) REFERENCES metodos_pago(id);

-- 6. Eliminar la columna vieja de texto libre
ALTER TABLE transacciones DROP COLUMN metodo_pago;


-- ===== ESTADOS DE PAGO =====

-- 7. Crear la tabla de estados de pago
CREATE TABLE estados_pago (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              nombre VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO estados_pago (nombre) VALUES ('APROBADO');
INSERT INTO estados_pago (nombre) VALUES ('RECHAZADO');

INSERT INTO estados_pago (nombre)
SELECT DISTINCT estado FROM transacciones
WHERE estado IS NOT NULL AND estado NOT IN (SELECT nombre FROM estados_pago);

ALTER TABLE transacciones ADD COLUMN estado_id BIGINT;

UPDATE transacciones t
    JOIN estados_pago e ON t.estado = e.nombre
    SET t.estado_id = e.id;

ALTER TABLE transacciones MODIFY COLUMN estado_id BIGINT NOT NULL;
ALTER TABLE transacciones
    ADD CONSTRAINT fk_transaccion_estado FOREIGN KEY (estado_id) REFERENCES estados_pago(id);

ALTER TABLE transacciones DROP COLUMN estado;