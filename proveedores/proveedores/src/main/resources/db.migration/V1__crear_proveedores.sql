CREATE TABLE proveedores (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     nombre VARCHAR(255) NOT NULL,
     pais VARCHAR(100) DEFAULT 'Desconocido'
);


INSERT INTO proveedores (id,nombre, pais) VALUES (1,'Panini', 'Italia');
INSERT INTO proveedores (id,nombre, pais) VALUES (2,'Bandai Spirits', 'Japón');
INSERT INTO proveedores (id,nombre, pais) VALUES (3,'K-Clothing', 'Chile');
INSERT INTO proveedores (id,nombre, pais) VALUES (4,'Distribuidora X', 'Chile');