-- Ejecutar una sola vez sobre la estructura vacía; todo el lote es atómico.
USE clinica_universitaria;
START TRANSACTION;
INSERT INTO Pacientes (codigo_paciente,nombre_completo,edad,telefono,correo_electronico,tipo_paciente,estado_paciente) VALUES
(1,'Ana López',20,'7000-0001','ana@example.test','Estudiante',1),
(2,'Luis Pérez',40,'7000-0002','luis@example.test','Docente',1),
(3,'Marta Díaz',32,'7000-0003','marta@example.test','Administrativo',1),
(4,'José Cruz',25,'7000-0004','jose@example.test','Visitante',1),
(5,'Sofía Ramos',22,'7000-0005','sofia@example.test','Estudiante',0);
INSERT INTO Doctores (codigo_doctor,nombre_completo,especialidad,telefono,correo_electronico,estado_doctor) VALUES
(1,'Carlos Flores','Medicina General','7100-0001','carlos@example.test',1),
(2,'Elena García','Psicología','7100-0002','elena@example.test',1),
(3,'Pedro Torres','Nutrición','7100-0003','pedro@example.test',1),
(4,'Laura Reyes','Fisioterapia','7100-0004','laura@example.test',1);
INSERT INTO CitasMedicas (codigo_paciente,codigo_doctor,fecha_cita,hora_cita,motivo_consulta,estado_cita) VALUES
(1,1,DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY),'09:00:00','Consulta general','Programada'),
(2,1,DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY),'10:00:00','Control','Programada'),
(3,2,DATE_ADD(CURRENT_DATE,INTERVAL 2 DAY),'09:00:00','Orientación','Programada'),
(4,3,DATE_ADD(CURRENT_DATE,INTERVAL 2 DAY),'11:00:00','Nutrición','Programada'),
(1,4,DATE_ADD(CURRENT_DATE,INTERVAL 3 DAY),'08:00:00','Rehabilitación','Programada'),
(2,1,DATE_SUB(CURRENT_DATE,INTERVAL 1 DAY),'09:00:00','Control previo','Atendida'),
(3,2,DATE_SUB(CURRENT_DATE,INTERVAL 2 DAY),'10:00:00','Consulta previa','Atendida'),
(4,1,DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY),'09:00:00','Cancelación histórica','Cancelada');
COMMIT;
