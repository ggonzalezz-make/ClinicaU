package com.clinica.modelo;
import com.clinica.enums.*;
import com.clinica.excepciones.DatosInvalidosException;
public class Paciente extends Persona {
 private int codigoPaciente;
 private int edad;
 private TipoPaciente tipoPaciente;
 private boolean estadoPaciente;
 public Paciente(int codigo, String nombre, String telefono, String correo, int edad, TipoPaciente tipoPaciente, boolean estadoPaciente) { super(nombre,telefono,correo);setCodigoPaciente(codigo);setEdad(edad);setTipoPaciente(tipoPaciente);setEstadoPaciente(estadoPaciente);}
 public int getCodigoPaciente(){return codigoPaciente;}
 public void setCodigoPaciente(int v){if(v<0) throw new DatosInvalidosException("Código inválido");codigoPaciente=v;}
 public int getEdad(){return edad;}
 public void setEdad(int v){if(v<0 || v>120) throw new DatosInvalidosException("Edad fuera de 0..120");edad=v;}
 public TipoPaciente getTipoPaciente(){return tipoPaciente;}
 public void setTipoPaciente(TipoPaciente v){Validacion.requerido(v,"tipoPaciente");tipoPaciente=v;}
 public boolean getEstadoPaciente(){return estadoPaciente;}
 public void setEstadoPaciente(boolean v){estadoPaciente=v;}
}