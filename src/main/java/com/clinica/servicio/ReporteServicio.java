package com.clinica.servicio;
import java.sql.*;
import java.time.*;
import java.util.*;
import com.clinica.conexion.*;
import com.clinica.dao.*;
import com.clinica.modelo.*;
public class ReporteServicio extends ServicioBase {
 public ReporteServicio(ConexionBD bd){super(bd);}
 public ResumenReporte obtenerResumen(){return leer(c->{
  String sql="SELECT (SELECT COUNT(*) FROM Pacientes) pacientes,(SELECT COUNT(*) FROM Doctores WHERE estado_doctor=1) doctores,(SELECT COUNT(*) FROM CitasMedicas WHERE estado_cita='Programada') programadas,(SELECT COUNT(*) FROM CitasMedicas WHERE estado_cita='Atendida') atendidas,(SELECT COUNT(*) FROM CitasMedicas WHERE estado_cita='Cancelada') canceladas";
  try(PreparedStatement s=c.prepareStatement(sql);ResultSet r=s.executeQuery()){r.next();return new ResumenReporte(r.getLong(1),r.getLong(2),r.getLong(3),r.getLong(4),r.getLong(5));}
 });}
 public List<DetalleCita> obtenerDetalleCitas(){return leer(c->new CitaMedicaDAO().detalles(c));}
 public List<CitaMedica> obtenerCitasPorDoctor(int codigo){return new CitaMedicaServicio(bd).obtenerCitasPorDoctor(codigo);}
 public List<CitaMedica> obtenerCitasPorPaciente(int codigo){return new CitaMedicaServicio(bd).obtenerCitasPorPaciente(codigo);}
 public List<CitaMedica> obtenerCitasPorFecha(LocalDate fecha){return new CitaMedicaServicio(bd).obtenerCitasPorFecha(fecha);}
}
