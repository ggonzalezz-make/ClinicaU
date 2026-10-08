package com.clinica.excepciones;
public class ConexionException extends ClinicaException {
 public ConexionException(String mensaje) { super(mensaje); }
 public ConexionException(String mensaje, Throwable causa) { super(mensaje, causa); }
}