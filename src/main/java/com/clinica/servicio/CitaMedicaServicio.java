package com.clinica.servicio;
import java.time.*;
import java.util.*;
import java.sql.*;
import com.clinica.conexion.*;
import com.clinica.dao.*;
import com.clinica.modelo.*;
import com.clinica.enums.*;
import com.clinica.excepciones.*;
public class CitaMedicaServicio extends ServicioBase {
 private final CitaMedicaDAO citas=new CitaMedicaDAO();
 private final PacienteDAO pacientes=new PacienteDAO();
 private final DoctorDAO doctores=new DoctorDAO();
 private final Clock reloj;
 public CitaMedicaServicio(ConexionBD bd){this(bd,Clock.system(ZoneId.of("America/El_Salvador")));}
 public CitaMedicaServicio(ConexionBD bd,Clock reloj){super(bd);this.reloj=Objects.requireNonNull(reloj);}
 public void validarHorario(LocalDate fecha,LocalTime hora){
  Validacion.requerido(fecha,"Fecha");Validacion.requerido(hora,"Hora");
  if(hora.getNano()!=0 || fecha.getYear()<1000 || fecha.getYear()>9999 || !LocalDateTime.of(fecha,hora).isAfter(LocalDateTime.now(reloj)))throw new DatosInvalidosException("La cita debe tener fecha y hora futuras, válidas para MySQL y sin fracciones de segundo");
 }
 private void validarParticipantes(Connection c,int paciente,int doctor)throws SQLException{
  // Orden de bloqueo uniforme: paciente, doctor. Los cambios de actividad usan las mismas filas.
  Paciente p=pacientes.buscar(c,paciente,true).orElseThrow(()->new PacienteNoEncontradoException("Paciente inexistente"));
  if(!p.getEstadoPaciente())throw new OperacionNoPermitidaException("Paciente inactivo");
  Doctor d=doctores.buscar(c,doctor,true).orElseThrow(()->new DoctorNoEncontradoException("Doctor inexistente"));
  if(!d.getEstadoDoctor())throw new OperacionNoPermitidaException("Doctor inactivo");
 }
 public CitaMedica registrarCita(CitaMedica v){
  Validacion.requerido(v,"Cita");validarHorario(v.getFechaCita(),v.getHoraCita());
  if(v.getCodigoCita()!=0 || v.getEstadoCita()!=EstadoCita.PROGRAMADA)throw new OperacionNoPermitidaException("Nueva cita debe tener código 0 y estado Programada");
  int id=transaccion(c->{validarParticipantes(c,v.getCodigoPaciente(),v.getCodigoDoctor());
   if(!citas.disponible(c,v.getCodigoDoctor(),v.getFechaCita(),v.getHoraCita(),0))throw new ConflictoHorarioException("Doctor ocupado");
   return citas.insertar(c,v);
  });v.setCodigoCita(id);return v;
 }
 public List<CitaMedica> obtenerCitas(){return leer(citas::listar);}
 public CitaMedica buscarCitaPorCodigo(int codigo){Validacion.codigo(codigo);return leer(c->citas.buscar(c,codigo,false).orElseThrow(()->new CitaNoEncontradaException("Cita inexistente")));}
 public List<CitaMedica> obtenerCitasPorPaciente(int codigo){new PacienteServicio(bd).buscarPacientePorCodigo(codigo);return leer(c->citas.porPaciente(c,codigo));}
 public List<CitaMedica> obtenerCitasPorDoctor(int codigo){new DoctorServicio(bd).buscarDoctorPorCodigo(codigo);return leer(c->citas.porDoctor(c,codigo));}
 public List<CitaMedica> obtenerCitasPorFecha(LocalDate fecha){Validacion.requerido(fecha,"Fecha");return leer(c->citas.porFecha(c,fecha));}
 public boolean consultarDisponibilidad(int doctor,LocalDate fecha,LocalTime hora){
  Validacion.codigo(doctor);validarHorario(fecha,hora);
  return leer(c->{Doctor d=doctores.buscar(c,doctor,false).orElseThrow(()->new DoctorNoEncontradoException("Doctor inexistente"));return d.getEstadoDoctor() && citas.disponible(c,doctor,fecha,hora,0);});
 }
 public CitaMedica cancelarCita(int codigo){return cambiarEstado(codigo,EstadoCita.CANCELADA);}
 public CitaMedica marcarCitaAtendida(int codigo){return cambiarEstado(codigo,EstadoCita.ATENDIDA);}
 private CitaMedica cambiarEstado(int codigo,EstadoCita estado){Validacion.codigo(codigo);return transaccion(c->{
  CitaMedica v=citas.buscar(c,codigo,true).orElseThrow(()->new CitaNoEncontradaException("Cita inexistente"));
  programada(v);
  if(estado==EstadoCita.ATENDIDA && LocalDateTime.of(v.getFechaCita(),v.getHoraCita()).isAfter(LocalDateTime.now(reloj)))throw new OperacionNoPermitidaException("No se puede atender una cita futura");
  v.setEstadoCita(estado);citas.actualizar(c,v);return v;
 });}
 private void programada(CitaMedica v){if(v.getEstadoCita()!=EstadoCita.PROGRAMADA)throw new OperacionNoPermitidaException("Solo una cita Programada permite esta operación");}
 public CitaMedica reprogramarCita(int codigo,LocalDate fecha,LocalTime hora){
  Validacion.codigo(codigo);validarHorario(fecha,hora);
  return transaccion(c->{CitaMedica v=citas.buscar(c,codigo,true).orElseThrow(()->new CitaNoEncontradaException("Cita inexistente"));programada(v);
   validarParticipantes(c,v.getCodigoPaciente(),v.getCodigoDoctor());
   if(!citas.disponible(c,v.getCodigoDoctor(),fecha,hora,codigo))throw new ConflictoHorarioException("Doctor ocupado");
   v.setFechaCita(fecha);v.setHoraCita(hora);citas.actualizar(c,v);return v;
  });
 }
}
