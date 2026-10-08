package com.clinica.modelo;
import java.time.*;
import com.clinica.enums.EstadoCita;
import com.clinica.excepciones.DatosInvalidosException;
public class CitaMedica {
 private int codigoCita;
 private int codigoPaciente;
 private int codigoDoctor;
 private LocalDate fechaCita;
 private LocalTime horaCita;
 private String motivoConsulta;
 private EstadoCita estadoCita;
 public CitaMedica(int codigoCita,int codigoPaciente,int codigoDoctor,LocalDate fechaCita,LocalTime horaCita,String motivoConsulta,EstadoCita estadoCita){setCodigoCita(codigoCita);setCodigoPaciente(codigoPaciente);setCodigoDoctor(codigoDoctor);setFechaCita(fechaCita);setHoraCita(horaCita);setMotivoConsulta(motivoConsulta);setEstadoCita(estadoCita);}
 public int getCodigoCita(){return codigoCita;}
 public void setCodigoCita(int v){if(v<0) throw new DatosInvalidosException("Código inválido");codigoCita=v;}
 public int getCodigoPaciente(){return codigoPaciente;}
 public void setCodigoPaciente(int v){Validacion.codigo(v);codigoPaciente=v;}
 public int getCodigoDoctor(){return codigoDoctor;}
 public void setCodigoDoctor(int v){Validacion.codigo(v);codigoDoctor=v;}
 public LocalDate getFechaCita(){return fechaCita;}
 public void setFechaCita(LocalDate v){Validacion.requerido(v,"fechaCita");fechaCita=v;}
 public LocalTime getHoraCita(){return horaCita;}
 public void setHoraCita(LocalTime v){Validacion.requerido(v,"horaCita");if(v.getNano()!=0) throw new DatosInvalidosException("Hora debe tener precisión de segundos");horaCita=v;}
 public String getMotivoConsulta(){return motivoConsulta;}
 public void setMotivoConsulta(String v){v=Validacion.texto(v,255,"Motivo");motivoConsulta=v;}
 public EstadoCita getEstadoCita(){return estadoCita;}
 public void setEstadoCita(EstadoCita v){Validacion.requerido(v,"estadoCita");estadoCita=v;}
}