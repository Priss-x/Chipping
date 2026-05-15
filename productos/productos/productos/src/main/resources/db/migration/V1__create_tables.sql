CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL
);

CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    precio INT NOT NULL,
    stock INT NOT NULL,
    marca VARCHAR(255),
    descripcion_corta VARCHAR(1000),
    categoria_id BIGINT,
    proveedor_id BIGINT,
    CONSTRAINT fk_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);


INSERT INTO categorias (descripcion) VALUES ('Mangas');
INSERT INTO categorias (descripcion) VALUES ('Figuras');
INSERT INTO categorias (descripcion) VALUES ('Figuras Coleccionables');
INSERT INTO categorias (descripcion) VALUES ('Álbum');
INSERT INTO categorias (descripcion) VALUES ('Poleras');


-- Mangas Hunter X Hunter (Panini)
INSERT INTO productos (nombre, precio, stock, descripcion_corta, categoria_id, proveedor_id)
VALUES ('Manga Hunter X Hunter Vol. 1', 10990, 5, 'Primer tomo Hunter X Hunter', 1, 1);

INSERT INTO productos (nombre, precio, stock, descripcion_corta, categoria_id, proveedor_id)
VALUES ('Manga Hunter X Hunter Vol. 2', 10990, 5, 'Segundo tomo Hunter X Hunter', 1, 1);

-- Figuras (Bandai Spirits)
INSERT INTO productos (nombre, precio, stock, descripcion_corta, categoria_id, proveedor_id)
VALUES ('Figura Killua Zoldyck', 75000, 3, 'Figura 15cm', 2, 2);

INSERT INTO productos (nombre, precio, stock, descripcion_corta, categoria_id, proveedor_id)
VALUES ('Figura Gon Frics', 90000, 4, 'Figura 20 cm', 3, 2);

-- Álbum (Panini)
INSERT INTO productos (nombre, precio, stock, descripcion_corta, categoria_id, proveedor_id)
VALUES ('Album One Piece + 20 sobres', 20990, 10, 'ONE PIECE RUMBO A EGGHEAD-2025', 4, 1);