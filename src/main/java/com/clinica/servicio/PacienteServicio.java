package com.clinica.servicio;
import java.util.*;
import com.clinica.conexion.*;
import com.clinica.dao.*;
import com.clinica.modelo.*;
import com.clinica.excepciones.*;
public class PacienteServicio extends ServicioBase {
 private final PacienteDAO dao=new PacienteDAO();
 public PacienteServicio(ConexionBD bd){super(bd);}
 public Paciente registrarPaciente(Paciente v){Validacion.requerido(v,"Paciente");if(v.getCodigoPaciente()!=0)throw new DatosInvalidosException("Nuevo registro debe tener código 0");int id=transaccion(c->dao.insertar(c,v));v.setCodigoPaciente(id);return v;}
 public List<Paciente> obtenerPacientes(){return leer(dao::listar);}
 public Paciente buscarPacientePorCodigo(int codigo){Validacion.codigo(codigo);return leer(c->dao.buscar(c,codigo,false).orElseThrow(()->new PacienteNoEncontradoException("Paciente inexistente: "+codigo)));}
 public Paciente actualizarPaciente(Paciente v){Validacion.requerido(v,"Paciente");Validacion.codigo(v.getCodigoPaciente());return transaccion(c->{dao.buscar(c,v.getCodigoPaciente(),true).orElseThrow(()->new PacienteNoEncontradoException("Paciente inexistente"));dao.actualizar(c,v);return v;});}
 public void desactivarPaciente(int codigo){Validacion.codigo(codigo);transaccion(c->{Paciente v=dao.buscar(c,codigo,true).orElseThrow(()->new PacienteNoEncontradoException("Paciente inexistente"));v.setEstadoPaciente(false);dao.actualizar(c,v);return null;});}
 public java.util.List<CitaMedica> obtenerCitasPorPaciente(int codigo){buscarPacientePorCodigo(codigo);return leer(c->new CitaMedicaDAO().porPaciente(c,codigo));}
}
