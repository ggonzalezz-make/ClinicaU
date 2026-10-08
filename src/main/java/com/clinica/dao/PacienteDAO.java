package com.clinica.dao;
import java.sql.*;
import java.util.*;
import com.clinica.modelo.*;
import com.clinica.enums.*;
public class PacienteDAO {
 public Paciente mapear(ResultSet r)throws SQLException{return new Paciente(r.getInt("codigo_paciente"),r.getString("nombre_completo"),r.getString("telefono"),r.getString("correo_electronico"),r.getInt("edad"),TipoPaciente.desdeValor(r.getString("tipo_paciente")),r.getBoolean("estado_paciente"));}
 public int insertar(Connection c,Paciente v)throws SQLException{return SQL.insertar(c,"INSERT INTO Pacientes (nombre_completo,telefono,correo_electronico,edad,tipo_paciente,estado_paciente) VALUES (?,?,?,?,?,?)",v.getNombreCompleto(),v.getTelefono(),v.getCorreoElectronico(),v.getEdad(),v.getTipoPaciente().getValor(),v.getEstadoPaciente());}
 public Optional<Paciente> buscar(Connection c,int codigo,boolean bloquear)throws SQLException{
  try(PreparedStatement s=c.prepareStatement("SELECT * FROM Pacientes WHERE codigo_paciente=?"+(bloquear?" FOR UPDATE":""))){s.setInt(1,codigo);try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(mapear(r)):Optional.empty();}}
 }
 public List<Paciente> listar(Connection c)throws SQLException{List<Paciente> lista=new ArrayList<>();try(PreparedStatement s=c.prepareStatement("SELECT * FROM Pacientes ORDER BY codigo_paciente");ResultSet r=s.executeQuery()){while(r.next())lista.add(mapear(r));}return lista;}
 public void actualizar(Connection c,Paciente v)throws SQLException{SQL.ejecutar(c,"UPDATE Pacientes SET nombre_completo=?,telefono=?,correo_electronico=?,edad=?,tipo_paciente=?,estado_paciente=? WHERE codigo_paciente=?",v.getNombreCompleto(),v.getTelefono(),v.getCorreoElectronico(),v.getEdad(),v.getTipoPaciente().getValor(),v.getEstadoPaciente(),v.getCodigoPaciente());}
}
