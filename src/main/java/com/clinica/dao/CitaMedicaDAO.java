package com.clinica.dao;
import java.sql.*;
import java.time.*;
import java.util.*;
import com.clinica.modelo.*;
import com.clinica.enums.*;
public class CitaMedicaDAO {
 public CitaMedica mapear(ResultSet r)throws SQLException{return new CitaMedica(r.getInt("codigo_cita"),r.getInt("codigo_paciente"),r.getInt("codigo_doctor"),r.getDate("fecha_cita").toLocalDate(),r.getTime("hora_cita").toLocalTime(),r.getString("motivo_consulta"),EstadoCita.desdeValor(r.getString("estado_cita")));}
 public int insertar(Connection c,CitaMedica v)throws SQLException{return SQL.insertar(c,"INSERT INTO CitasMedicas (codigo_paciente,codigo_doctor,fecha_cita,hora_cita,motivo_consulta,estado_cita) VALUES (?,?,?,?,?,?)",v.getCodigoPaciente(),v.getCodigoDoctor(),v.getFechaCita(),v.getHoraCita(),v.getMotivoConsulta(),v.getEstadoCita().getValor());}
 public Optional<CitaMedica> buscar(Connection c,int codigo,boolean bloquear)throws SQLException{
  try(PreparedStatement s=c.prepareStatement("SELECT * FROM CitasMedicas WHERE codigo_cita=?"+(bloquear?" FOR UPDATE":""))){s.setInt(1,codigo);try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(mapear(r)):Optional.empty();}}
 }
 public List<CitaMedica> listar(Connection c)throws SQLException{return consultar(c,"",null);}
 public List<CitaMedica> porPaciente(Connection c,int id)throws SQLException{return consultar(c,"codigo_paciente",id);}
 public List<CitaMedica> porDoctor(Connection c,int id)throws SQLException{return consultar(c,"codigo_doctor",id);}
 public List<CitaMedica> porFecha(Connection c,LocalDate fecha)throws SQLException{return consultar(c,"fecha_cita",fecha);}
 private List<CitaMedica> consultar(Connection c,String columna,Object valor)throws SQLException{
  List<CitaMedica> lista=new ArrayList<>();
  try(PreparedStatement s=c.prepareStatement("SELECT * FROM CitasMedicas"+(columna.isEmpty()?"":" WHERE "+columna+"=?")+" ORDER BY fecha_cita,hora_cita,codigo_cita")){
   if(valor!=null)s.setObject(1,valor);try(ResultSet r=s.executeQuery()){while(r.next())lista.add(mapear(r));}
  }return lista;
 }
 public boolean disponible(Connection c,int doctor,LocalDate fecha,LocalTime hora,int excluir)throws SQLException{
  try(PreparedStatement s=c.prepareStatement("SELECT codigo_cita FROM CitasMedicas WHERE codigo_doctor=? AND fecha_cita=? AND hora_cita=? AND estado_cita='Programada' AND codigo_cita<>?")){
   SQL.parametros(s,doctor,fecha,hora,excluir);try(ResultSet r=s.executeQuery()){return !r.next();}
  }
 }
 public void actualizar(Connection c,CitaMedica v)throws SQLException{SQL.ejecutar(c,"UPDATE CitasMedicas SET fecha_cita=?,hora_cita=?,estado_cita=? WHERE codigo_cita=?",v.getFechaCita(),v.getHoraCita(),v.getEstadoCita().getValor(),v.getCodigoCita());}
 public List<DetalleCita> detalles(Connection c)throws SQLException{
  List<DetalleCita> lista=new ArrayList<>();
  try(PreparedStatement s=c.prepareStatement("SELECT c.*,p.nombre_completo AS paciente,d.nombre_completo AS doctor,d.especialidad FROM CitasMedicas c JOIN Pacientes p ON p.codigo_paciente=c.codigo_paciente JOIN Doctores d ON d.codigo_doctor=c.codigo_doctor ORDER BY c.fecha_cita,c.hora_cita,c.codigo_cita");ResultSet r=s.executeQuery()){
   while(r.next())lista.add(new DetalleCita(mapear(r),r.getString("paciente"),r.getString("doctor"),r.getString("especialidad")));
  }return lista;
 }
}
