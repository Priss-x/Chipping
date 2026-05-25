CREATE TABLE notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50),
    mensaje VARCHAR(255),
    fecha DATETIME
);