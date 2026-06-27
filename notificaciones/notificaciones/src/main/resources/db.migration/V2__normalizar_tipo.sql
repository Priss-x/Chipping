CREATE TABLE tipos_notificacion (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipos_notificacion (nombre) VALUES ('STOCK');
INSERT INTO tipos_notificacion (nombre) VALUES ('ERROR');

INSERT INTO tipos_notificacion (nombre)
SELECT DISTINCT tipo FROM notificaciones
WHERE tipo IS NOT NULL AND tipo NOT IN (SELECT nombre FROM tipos_notificacion);

ALTER TABLE notificaciones ADD COLUMN tipo_id BIGINT;

UPDATE notificaciones n
    JOIN tipos_notificacion t ON n.tipo = t.nombre
    SET n.tipo_id = t.id;

ALTER TABLE notificaciones MODIFY COLUMN tipo_id BIGINT NOT NULL;
ALTER TABLE notificaciones
    ADD CONSTRAINT fk_notificacion_tipo FOREIGN KEY (tipo_id) REFERENCES tipos_notificacion(id);

ALTER TABLE notificaciones DROP COLUMN tipo;