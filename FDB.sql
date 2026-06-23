CREATE DATABASE ferreteria_db;
USE ferreteria_db;


CREATE TABLE producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(100),
    precio DECIMAL(10,2),
    stock INT
);


INSERT INTO producto (nombre, categoria, precio, stock) VALUES
('Martillo', 'Herramientas', 25.90, 100),
('Taladro', 'Electricas', 350.00, 30),
('Clavos x100', 'Fijaciones', 10.50, 200),
('Serrucho', 'Corte', 40.00, 50);



CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(8) NOT NULL UNIQUE,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(9) NOT NULL,       
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO'
);


INSERT INTO cliente (nombres, apellidos, dni, direccion, telefono) VALUES
('Juan', 'Pérez', '12345678', 'Av. Siempre Viva 123', '987654321'),
('María', 'Gómez', '87654321', 'Jr. Las Flores 456', '912345678');

ALTER TABLE cliente ADD COLUMN usuario_id BIGINT UNIQUE;


CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'USER',
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO'
);

ALTER TABLE cliente ADD CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id);


INSERT INTO usuario (username, password, rol) VALUES
('admin', '$2a$12$Rn4EaBaJMo975j4kSahRIuB5.cBwJWeliuWZlnL0F08/K4y1zLStm', 'ADMIN');

CREATE TABLE carrito_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    producto_id BIGINT,
    cantidad INT,
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

CREATE TABLE venta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cliente_id BIGINT,
    total DECIMAL(10,2),
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE detalle_venta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venta_id BIGINT,
    producto_id BIGINT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2),
    subtotal DECIMAL(10,2),
    FOREIGN KEY (venta_id) REFERENCES venta(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

CREATE TABLE movimiento_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT,
    tipo ENUM('ENTRADA', 'SALIDA') NOT NULL,
    cantidad INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion VARCHAR(255),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);