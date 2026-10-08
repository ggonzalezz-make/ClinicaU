package com.clinica.excepciones;
public class CitaNoEncontradaException extends ClinicaException {
 public CitaNoEncontradaException(String mensaje) { super(mensaje); }
 public CitaNoEncontradaException(String mensaje, Throwable causa) { super(mensaje, causa); }
}