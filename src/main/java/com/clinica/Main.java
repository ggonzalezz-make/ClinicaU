package com.clinica;

import com.clinica.conexion.ConexionBD;
import com.clinica.modelo.ResumenReporte;
import com.clinica.servicio.DoctorServicio;
import com.clinica.servicio.PacienteServicio;
import com.clinica.servicio.ReporteServicio;

/** Ejecución directa de consultas; no solicita datos ni modifica registros. */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        ConexionBD bd = new ConexionBD();
        ResumenReporte resumen = new ReporteServicio(bd).obtenerResumen();

        System.out.println("Conexión a MySQL establecida correctamente.");
        System.out.println("Total de pacientes: " + resumen.totalPacientes());
        System.out.println("Doctores activos: " + resumen.doctoresActivos());
        System.out.println("Citas programadas: " + resumen.citasProgramadas());
        System.out.println("Citas atendidas: " + resumen.citasAtendidas());
        System.out.println("Citas canceladas: " + resumen.citasCanceladas());

        System.out.println("Pacientes registrados:");
        new PacienteServicio(bd).obtenerPacientes().forEach(p ->
            System.out.println(p.getCodigoPaciente() + " - " + p.getNombreCompleto()));

        System.out.println("Doctores registrados:");
        new DoctorServicio(bd).obtenerDoctores().forEach(d ->
            System.out.println(d.getCodigoDoctor() + " - " + d.getNombreCompleto()
                + " - " + d.getEspecialidad().getValor()));
    }
}
