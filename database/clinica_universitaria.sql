-- MySQL >= 8.0.16 (CHECK efectivo). No elimina datos existentes.
CREATE DATABASE IF NOT EXISTS clinica_universitaria CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;
USE clinica_universitaria;
CREATE TABLE Pacientes (
 codigo_paciente INT AUTO_INCREMENT PRIMARY KEY,
 nombre_completo VARCHAR(150) NOT NULL CHECK (CHAR_LENGTH(TRIM(nombre_completo)) > 0),
 edad INT NOT NULL CHECK (edad BETWEEN 0 AND 120),
 telefono VARCHAR(20) NOT NULL CHECK (CHAR_LENGTH(TRIM(telefono)) > 0),
 correo_electronico VARCHAR(150) NOT NULL CHECK (CHAR_LENGTH(TRIM(correo_electronico)) > 0),
 tipo_paciente VARCHAR(30) NOT NULL CHECK (tipo_paciente IN ('Estudiante','Docente','Administrativo','Visitante')),
 estado_paciente BOOLEAN NOT NULL DEFAULT TRUE CHECK (estado_paciente IN (0,1))
) ENGINE=InnoDB;
CREATE TABLE Doctores (
 codigo_doctor INT AUTO_INCREMENT PRIMARY KEY,
 nombre_completo VARCHAR(150) NOT NULL CHECK (CHAR_LENGTH(TRIM(nombre_completo)) > 0),
 especialidad VARCHAR(50) NOT NULL CHECK (especialidad IN ('Medicina General','Psicología','Nutrición','Fisioterapia')),
 telefono VARCHAR(20) NOT NULL CHECK (CHAR_LENGTH(TRIM(telefono)) > 0),
 correo_electronico VARCHAR(150) NOT NULL CHECK (CHAR_LENGTH(TRIM(correo_electronico)) > 0),
 estado_doctor BOOLEAN NOT NULL DEFAULT TRUE CHECK (estado_doctor IN (0,1))
) ENGINE=InnoDB;
CREATE TABLE CitasMedicas (
 codigo_cita INT AUTO_INCREMENT PRIMARY KEY,
 codigo_paciente INT NOT NULL,
 codigo_doctor INT NOT NULL,
 fecha_cita DATE NOT NULL CHECK (fecha_cita BETWEEN '1000-01-01' AND '9999-12-31'),
 hora_cita TIME NOT NULL CHECK (hora_cita >= '00:00:00' AND hora_cita < '24:00:00'),
 motivo_consulta VARCHAR(255) NOT NULL CHECK (CHAR_LENGTH(TRIM(motivo_consulta)) > 0),
 estado_cita VARCHAR(20) NOT NULL CHECK (estado_cita IN ('Programada','Atendida','Cancelada')),
 -- NULL no participa en la unicidad: solamente Programada reserva el horario.
 reserva_programada TINYINT GENERATED ALWAYS AS (CASE WHEN estado_cita = 'Programada' THEN 1 ELSE NULL END) STORED,
 CONSTRAINT fk_cita_paciente FOREIGN KEY (codigo_paciente) REFERENCES Pacientes(codigo_paciente) ON DELETE RESTRICT ON UPDATE RESTRICT,
 CONSTRAINT fk_cita_doctor FOREIGN KEY (codigo_doctor) REFERENCES Doctores(codigo_doctor) ON DELETE RESTRICT ON UPDATE RESTRICT,
 CONSTRAINT uq_doctor_horario UNIQUE (codigo_doctor,fecha_cita,hora_cita,reserva_programada),
 INDEX idx_citas_paciente (codigo_paciente,fecha_cita),
 INDEX idx_citas_fecha (fecha_cita,hora_cita),
 INDEX idx_citas_estado (estado_cita)
) ENGINE=InnoDB;
