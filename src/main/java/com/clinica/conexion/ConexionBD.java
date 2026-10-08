package com.clinica.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.clinica.excepciones.ConexionException;

public class ConexionBD {
    public Connection abrirConexion() {
        try {
            // Carga explícita del controlador, como en la clase.
            Class.forName("com.mysql.jdbc.Driver");

            Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost/clinica_universitaria",
                "root",
                ""
            );
            // Quien recibe la conexión debe cerrarla con try-with-resources.
            return conexion;
        } catch (ClassNotFoundException e) {
            throw new ConexionException("No se encontró el controlador JDBC de MySQL", e);
        } catch (SQLException e) {
            throw new ConexionException("No se pudo conectar a la base clinica_universitaria", e);
        }
    }
}
