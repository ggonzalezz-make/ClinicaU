package com.clinica.dao;
import java.sql.*;
import java.util.*;
import com.clinica.modelo.*;
import com.clinica.enums.*;
public class DoctorDAO {
 public Doctor mapear(ResultSet r)throws SQLException{return new Doctor(r.getInt("codigo_doctor"),r.getString("nombre_completo"),r.getString("telefono"),r.getString("correo_electronico"),Especialidad.desdeValor(r.getString("especialidad")),r.getBoolean("estado_doctor"));}
 public int insertar(Connection c,Doctor v)throws SQLException{return SQL.insertar(c,"INSERT INTO Doctores (nombre_completo,telefono,correo_electronico,especialidad,estado_doctor) VALUES (?,?,?,?,?)",v.getNombreCompleto(),v.getTelefono(),v.getCorreoElectronico(),v.getEspecialidad().getValor(),v.getEstadoDoctor());}
 public Optional<Doctor> buscar(Connection c,int codigo,boolean bloquear)throws SQLException{
  try(PreparedStatement s=c.prepareStatement("SELECT * FROM Doctores WHERE codigo_doctor=?"+(bloquear?" FOR UPDATE":""))){s.setInt(1,codigo);try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(mapear(r)):Optional.empty();}}
 }
 public List<Doctor> listar(Connection c)throws SQLException{List<Doctor> lista=new ArrayList<>();try(PreparedStatement s=c.prepareStatement("SELECT * FROM Doctores ORDER BY codigo_doctor");ResultSet r=s.executeQuery()){while(r.next())lista.add(mapear(r));}return lista;}
 public void actualizar(Connection c,Doctor v)throws SQLException{SQL.ejecutar(c,"UPDATE Doctores SET nombre_completo=?,telefono=?,correo_electronico=?,especialidad=?,estado_doctor=? WHERE codigo_doctor=?",v.getNombreCompleto(),v.getTelefono(),v.getCorreoElectronico(),v.getEspecialidad().getValor(),v.getEstadoDoctor(),v.getCodigoDoctor());}
}
