package com.clinica.enums;
public enum Especialidad {
MEDICINA_GENERAL("Medicina General"),
PSICOLOGIA("Psicología"),
NUTRICION("Nutrición"),
FISIOTERAPIA("Fisioterapia");
 private final String valor;
 Especialidad(String valor) { this.valor=valor; }
 public String getValor() { return valor; }
 public static Especialidad desdeValor(String valor) {
  for (Especialidad v : values()) if (v.valor.equals(valor)) return v;
  throw new IllegalArgumentException("Valor desconocido: " + valor);
 }
}
