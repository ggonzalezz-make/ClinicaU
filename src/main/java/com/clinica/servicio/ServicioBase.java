package com.clinica.servicio;
import java.sql.*;
import com.clinica.conexion.ConexionBD;
import com.clinica.dao.SQL;
abstract class ServicioBase {
 protected final ConexionBD bd;
 protected ServicioBase(ConexionBD bd){this.bd=java.util.Objects.requireNonNull(bd);}
 @FunctionalInterface protected interface Trabajo<T>{T ejecutar(Connection c)throws SQLException;}
 protected <T> T leer(Trabajo<T> trabajo){try(Connection c=bd.abrirConexion()){return trabajo.ejecutar(c);}catch(SQLException e){throw SQL.error(e);}}
 protected <T> T transaccion(Trabajo<T> trabajo){
  try(Connection c=bd.abrirConexion()){
   c.setAutoCommit(false);
   try{T resultado=trabajo.ejecutar(c);c.commit();return resultado;}
   catch(SQLException|RuntimeException e){try{c.rollback();}catch(SQLException rollback){e.addSuppressed(rollback);}throw e;}
  }catch(SQLException e){throw SQL.errorCita(e);}
 }
}
