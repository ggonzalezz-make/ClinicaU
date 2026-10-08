package com.clinica.excepciones;
public class PacienteNoEncontradoException extends ClinicaException {
 public PacienteNoEncontradoException(String mensaje) { super(mensaje); }
 public PacienteNoEncontradoException(String mensaje, Throwable causa) { super(mensaje, causa); }
}