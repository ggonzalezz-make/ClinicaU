package com.clinica.enums;
public enum TipoPaciente {
ESTUDIANTE("Estudiante"),
DOCENTE("Docente"),
ADMINISTRATIVO("Administrativo"),
VISITANTE("Visitante");
 private final String valor;
 TipoPaciente(String valor) { this.valor=valor; }
 public String getValor() { return valor; }
 public static TipoPaciente desdeValor(String valor) {
  for (TipoPaciente v : values()) if (v.valor.equals(valor)) return v;
  throw new IllegalArgumentException("Valor desconocido: " + valor);
 }
}
