CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (nombre) VALUES ('CLIENTE');
INSERT INTO roles (nombre) VALUES ('ADMIN');

INSERT INTO roles (nombre)
SELECT DISTINCT role FROM usuarios
WHERE role IS NOT NULL AND role NOT IN (SELECT nombre FROM roles);

ALTER TABLE usuarios ADD COLUMN role_id BIGINT;

UPDATE usuarios u
    JOIN roles r ON u.role = r.nombre
    SET u.role_id = r.id;

UPDATE usuarios u
SET u.role_id = (SELECT id FROM roles WHERE nombre = 'CLIENTE')
WHERE u.role_id IS NULL;

ALTER TABLE usuarios MODIFY COLUMN role_id BIGINT NOT NULL;
ALTER TABLE usuarios
    ADD CONSTRAINT fk_usuario_role FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE usuarios DROP COLUMN role;