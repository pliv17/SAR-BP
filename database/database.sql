
DROP TABLE IF EXISTS alerta;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    doc_identificacion VARCHAR(11) NOT NULL UNIQUE CHECK (doc_identificacion ~ '^[0-9]{8}$' OR doc_identificacion ~ '^[0-9]{11}$'),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN', 'LIDER')),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE alerta (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    fecha_creacion TIMESTAMPTZ DEFAULT NOW(),
    categoria_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_alerta_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id),
    CONSTRAINT fk_alerta_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- password = "password123" hashed with BCrypt
INSERT INTO usuario (nombres, apellidos, doc_identificacion, username, password, rol, estado) VALUES 
('Admin', 'Central', '12345678901', 'admin_central', '$2a$12$7MWKFvhb9RHTYX7SrbnlvuFKEdJtflZsB9uAHHfzYV2GdcUnYjeiK', 'ADMIN', 'ACTIVO'),
('Carlos David', 'García Sánchez', '67895432', 'carlos_garcia', '$2a$12$7MWKFvhb9RHTYX7SrbnlvuFKEdJtflZsB9uAHHfzYV2GdcUnYjeiK', 'LIDER', 'ACTIVO'),
('Luisa', 'Sandoval Llanos', '69007434', 'luisa_sandoval', '$2a$12$7MWKFvhb9RHTYX7SrbnlvuFKEdJtflZsB9uAHHfzYV2GdcUnYjeiK', 'LIDER', 'ACTIVO'),
('Esther Carolina', 'Sosa Maldonado', '14126478987', 'esther_sosa', '$2a$12$7MWKFvhb9RHTYX7SrbnlvuFKEdJtflZsB9uAHHfzYV2GdcUnYjeiK', 'LIDER', 'INACTIVO');

INSERT INTO categoria (nombre, estado) VALUES 
('PLAGA', 'ACTIVO'),
('AGUA Y DESAGÜE', 'ACTIVO'),
('SEGURIDAD', 'ACTIVO'),
('INFRAESTRUCTURA', 'ACTIVO');

INSERT INTO alerta (titulo, descripcion, categoria_id, usuario_id, estado) VALUES 
('Bloqueo de canal de regadío', 'El canal principal del sector norte está obstruido con desmonte y basura.', 2, 2, 'ACTIVO'),
('Presencia de plaga de grillos', 'Se ha detectado una alta concentración de grillos en los almacenes.', 1, 2, 'ACTIVO'),
('Poste a punto de caer', 'Poste de luz inclinado en la avenida principal.', 4, 3, 'INACTIVO');