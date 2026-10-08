package com.clinica.modelo;
import com.clinica.excepciones.DatosInvalidosException;
public final class Validacion {
 private Validacion() {}
 public static String texto(String v,int max,String campo) {
  if(v==null || v.isBlank() || v.length()>max) throw new DatosInvalidosException(campo+": requerido, máximo "+max+" caracteres");
  return v.trim();
 }
 public static String telefono(String v) { v=texto(v,20,"Teléfono"); if(!v.matches("[+0-9() .-]{7,20}")) throw new DatosInvalidosException("Teléfono inválido"); return v; }
 public static String correo(String v) { v=texto(v,150,"Correo"); if(!v.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new DatosInvalidosException("Correo inválido"); return v; }
 public static void codigo(int v) { if(v<=0) throw new DatosInvalidosException("Código debe ser positivo"); }
 public static <T> T requerido(T v,String campo) { if(v==null) throw new DatosInvalidosException(campo+" requerido"); return v; }
}
