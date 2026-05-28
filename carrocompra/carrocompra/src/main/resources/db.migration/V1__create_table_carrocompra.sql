DROP TABLE IF EXISTS item_carro;
DROP TABLE IF EXISTS carro;

CREATE TABLE carros (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   usuario_id BIGINT NOT NULL,
   estado VARCHAR(20) NOT NULL,
   fecha_creacion DATETIME NOT NULL,
   fecha_actualizacion DATETIME NOT NULL
);

CREATE TABLE items_carro (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   carro_id BIGINT NOT NULL,
   producto_id BIGINT NOT NULL,
   cantidad INT NOT NULL,
   precio_unitario INT NOT NULL,

   CONSTRAINT fk_carro
        FOREIGN KEY (carro_id)
        REFERENCES carros(id)
        ON DELETE CASCADE
);