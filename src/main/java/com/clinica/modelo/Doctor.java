package com.clinica.modelo;
import com.clinica.enums.*;
import com.clinica.excepciones.DatosInvalidosException;
public class Doctor extends Persona {
 private int codigoDoctor;
 private Especialidad especialidad;
 private boolean estadoDoctor;
 public Doctor(int codigo, String nombre, String telefono, String correo, Especialidad especialidad, boolean estadoDoctor) { super(nombre,telefono,correo);setCodigoDoctor(codigo);setEspecialidad(especialidad);setEstadoDoctor(estadoDoctor);}
 public int getCodigoDoctor(){return codigoDoctor;}
 public void setCodigoDoctor(int v){if(v<0) throw new DatosInvalidosException("Código inválido");codigoDoctor=v;}
 public Especialidad getEspecialidad(){return especialidad;}
 public void setEspecialidad(Especialidad v){Validacion.requerido(v,"especialidad");especialidad=v;}
 public boolean getEstadoDoctor(){return estadoDoctor;}
 public void setEstadoDoctor(boolean v){estadoDoctor=v;}
}