package com.clinica.excepciones;
public class ClinicaException extends RuntimeException {
 public ClinicaException(String mensaje) { super(mensaje); }
 public ClinicaException(String mensaje, Throwable causa) { super(mensaje, causa); }
}