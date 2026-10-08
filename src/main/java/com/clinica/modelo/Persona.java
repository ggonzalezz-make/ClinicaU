package com.clinica.modelo;
public abstract class Persona {
 private String nombreCompleto, telefono, correoElectronico;
 protected Persona(String nombre,String telefono,String correo) { setNombreCompleto(nombre);setTelefono(telefono);setCorreoElectronico(correo); }
 public String getNombreCompleto(){return nombreCompleto;}
 public void setNombreCompleto(String v){nombreCompleto=Validacion.texto(v,150,"Nombre");}
 public String getTelefono(){return telefono;}
 public void setTelefono(String v){telefono=Validacion.telefono(v);}
 public String getCorreoElectronico(){return correoElectronico;}
 public void setCorreoElectronico(String v){correoElectronico=Validacion.correo(v);}
}
