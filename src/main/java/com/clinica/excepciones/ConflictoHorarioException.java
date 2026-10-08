package com.clinica.excepciones;
public class ConflictoHorarioException extends ClinicaException {
 public ConflictoHorarioException(String mensaje) { super(mensaje); }
 public ConflictoHorarioException(String mensaje, Throwable causa) { super(mensaje, causa); }
}