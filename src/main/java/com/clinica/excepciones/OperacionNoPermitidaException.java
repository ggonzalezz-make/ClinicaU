package com.clinica.excepciones;
public class OperacionNoPermitidaException extends ClinicaException {
 public OperacionNoPermitidaException(String mensaje) { super(mensaje); }
 public OperacionNoPermitidaException(String mensaje, Throwable causa) { super(mensaje, causa); }
}