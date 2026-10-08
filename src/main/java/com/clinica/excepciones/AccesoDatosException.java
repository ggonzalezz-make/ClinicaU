package com.clinica.excepciones;
public class AccesoDatosException extends ClinicaException {
 public AccesoDatosException(String mensaje) { super(mensaje); }
 public AccesoDatosException(String mensaje, Throwable causa) { super(mensaje, causa); }
}