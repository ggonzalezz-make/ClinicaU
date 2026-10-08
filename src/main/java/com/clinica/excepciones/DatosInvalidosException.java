package com.clinica.excepciones;
public class DatosInvalidosException extends ClinicaException {
 public DatosInvalidosException(String mensaje) { super(mensaje); }
 public DatosInvalidosException(String mensaje, Throwable causa) { super(mensaje, causa); }
}