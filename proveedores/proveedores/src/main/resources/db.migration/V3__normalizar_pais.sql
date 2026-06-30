CREATE TABLE paises (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO paises (nombre)
SELECT DISTINCT pais FROM proveedores WHERE pais IS NOT NULL;

ALTER TABLE proveedores ADD COLUMN pais_id BIGINT;

UPDATE proveedores p
    JOIN paises pa ON p.pais = pa.nombre
    SET p.pais_id = pa.id;

ALTER TABLE proveedores
    ADD CONSTRAINT fk_proveedor_pais FOREIGN KEY (pais_id) REFERENCES paises(id);

ALTER TABLE proveedores DROP COLUMN pais;