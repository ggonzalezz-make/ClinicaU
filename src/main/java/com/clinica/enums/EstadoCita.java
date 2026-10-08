package com.clinica.enums;
public enum EstadoCita {
PROGRAMADA("Programada"),
ATENDIDA("Atendida"),
CANCELADA("Cancelada");
 private final String valor;
 EstadoCita(String valor) { this.valor=valor; }
 public String getValor() { return valor; }
 public static EstadoCita desdeValor(String valor) {
  for (EstadoCita v : values()) if (v.valor.equals(valor)) return v;
  throw new IllegalArgumentException("Valor desconocido: " + valor);
 }
}
