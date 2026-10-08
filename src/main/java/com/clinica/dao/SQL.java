package com.clinica.dao;
import java.sql.*;
import com.clinica.excepciones.*;
public final class SQL {
 private SQL(){}
 public static AccesoDatosException error(SQLException e){return new AccesoDatosException("Error de acceso a datos (SQLState "+e.getSQLState()+")",e);}
 public static RuntimeException errorCita(SQLException e){
  if(e.getErrorCode()==1062 && e.getMessage().contains("uq_doctor_horario")) return new ConflictoHorarioException("Doctor ocupado en ese horario",e);
  return error(e);
 }
 public static int insertar(Connection c,String sql,Object... args)throws SQLException{
  try(PreparedStatement s=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){parametros(s,args);s.executeUpdate();try(ResultSet r=s.getGeneratedKeys()){if(!r.next())throw new SQLException("No se obtuvo código generado");return r.getInt(1);}}
 }
 public static int ejecutar(Connection c,String sql,Object...args)throws SQLException{try(PreparedStatement s=c.prepareStatement(sql)){parametros(s,args);return s.executeUpdate();}}
 public static void parametros(PreparedStatement s,Object...args)throws SQLException{for(int i=0;i<args.length;i++)s.setObject(i+1,args[i]);}
}
