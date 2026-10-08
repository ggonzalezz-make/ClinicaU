package com.clinica.excepciones;
public class DoctorNoEncontradoException extends ClinicaException {
 public DoctorNoEncontradoException(String mensaje) { super(mensaje); }
 public DoctorNoEncontradoException(String mensaje, Throwable causa) { super(mensaje, causa); }
}