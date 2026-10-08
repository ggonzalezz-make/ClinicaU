package com.clinica.servicio;
import java.util.*;
import com.clinica.conexion.*;
import com.clinica.dao.*;
import com.clinica.modelo.*;
import com.clinica.excepciones.*;
public class DoctorServicio extends ServicioBase {
 private final DoctorDAO dao=new DoctorDAO();
 public DoctorServicio(ConexionBD bd){super(bd);}
 public Doctor registrarDoctor(Doctor v){Validacion.requerido(v,"Doctor");if(v.getCodigoDoctor()!=0)throw new DatosInvalidosException("Nuevo registro debe tener código 0");int id=transaccion(c->dao.insertar(c,v));v.setCodigoDoctor(id);return v;}
 public List<Doctor> obtenerDoctores(){return leer(dao::listar);}
 public Doctor buscarDoctorPorCodigo(int codigo){Validacion.codigo(codigo);return leer(c->dao.buscar(c,codigo,false).orElseThrow(()->new DoctorNoEncontradoException("Doctor inexistente: "+codigo)));}
 public Doctor actualizarDoctor(Doctor v){Validacion.requerido(v,"Doctor");Validacion.codigo(v.getCodigoDoctor());return transaccion(c->{dao.buscar(c,v.getCodigoDoctor(),true).orElseThrow(()->new DoctorNoEncontradoException("Doctor inexistente"));dao.actualizar(c,v);return v;});}
 public void cambiarEstadoDoctor(int codigo,boolean activo){Validacion.codigo(codigo);transaccion(c->{Doctor v=dao.buscar(c,codigo,true).orElseThrow(()->new DoctorNoEncontradoException("Doctor inexistente"));v.setEstadoDoctor(activo);dao.actualizar(c,v);return null;});}
 public boolean consultarDisponibilidad(int codigo,java.time.LocalDate fecha,java.time.LocalTime hora){return new CitaMedicaServicio(bd).consultarDisponibilidad(codigo,fecha,hora);}
}
