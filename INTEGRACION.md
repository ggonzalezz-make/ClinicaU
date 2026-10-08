# Integración del backend

Añadir la carpeta de clases compiladas `out` y `lib/mysql-connector-j-9.1.0.jar` al classpath del proyecto consumidor, configurar JDBC y construir servicios con una misma instancia de ConexionBD. También se pueden importar los paquetes fuente actuales y agregar el driver como biblioteca del IDE. Los comandos de compilación y ejecución están en README.md. Cada llamada abre y cierra su conexión. No mantener una conexión en la interfaz. Los modelos son mutables: no compartir un objeto que otra tarea esté modificando. La instancia ConexionBD y el reloj pueden compartirse; los servicios no mantienen conexiones ni transacciones entre llamadas.

```java
import com.clinica.conexion.ConexionBD;
import com.clinica.modelo.*;
import com.clinica.enums.*;
import com.clinica.servicio.*;
import com.clinica.excepciones.*;
import java.time.*;

// Dentro del método de la futura aplicación:
ConexionBD bd = new ConexionBD();
PacienteServicio pacientes = new PacienteServicio(bd);
DoctorServicio doctores = new DoctorServicio(bd);
CitaMedicaServicio citas = new CitaMedicaServicio(bd);
ReporteServicio reportes = new ReporteServicio(bd);
Paciente p = pacientes.registrarPaciente(new Paciente(
    0, "Ana López", "7000-0001", "ana@example.test", 20,
    TipoPaciente.ESTUDIANTE, true));
java.util.List<Doctor> lista = doctores.obtenerDoctores();
// Elegir un doctor existente desde la interfaz; no asumir que la lista tiene elementos.
Doctor d = lista.stream().filter(Doctor::getEstadoDoctor).findFirst()
    .orElseThrow(() -> new OperacionNoPermitidaException("No hay doctores activos"));
LocalDate fecha = LocalDate.now(ZoneId.of("America/El_Salvador")).plusDays(1);
LocalTime hora = LocalTime.of(9, 0);
boolean disponible = doctores.consultarDisponibilidad(d.getCodigoDoctor(), fecha, hora);
// La consulta es orientativa; registrar siempre puede lanzar ConflictoHorarioException.
CitaMedica cita = citas.registrarCita(new CitaMedica(
    0, p.getCodigoPaciente(), d.getCodigoDoctor(), fecha, hora,
    "Consulta general", EstadoCita.PROGRAMADA));
citas.cancelarCita(cita.getCodigoCita());
ResumenReporte resumen = reportes.obtenerResumen();
java.util.List<DetalleCita> detalle = reportes.obtenerDetalleCitas();
```

## Contrato de servicios

Los constructores de los cuatro servicios reciben `ConexionBD`. CitaMedicaServicio admite además `Clock` para controlar la fecha y hora de referencia; el constructor normal usa America/El_Salvador. Listas sin coincidencias son vacías; búsqueda individual o filtro por una persona inexistente lanza su excepción específica. Los métodos no retornan null.

| Servicio / método | Parámetros | Retorno / comportamiento |
|---|---|---|
| PacienteServicio.registrarPaciente | Paciente con código 0 | Paciente con código generado; modifica código tras COMMIT |
| obtenerPacientes | ninguno | List de todos los Paciente, incluidos inactivos |
| buscarPacientePorCodigo | int código positivo | Paciente o PacienteNoEncontradoException |
| actualizarPaciente | Paciente con código existente | Paciente actualizado; permite cambiar sus datos y actividad |
| desactivarPaciente | int código | void; conserva historial, repetición permitida |
| obtenerCitasPorPaciente | int código | List de CitaMedica en todos los estados |
| DoctorServicio.registrarDoctor | Doctor con código 0 | Doctor con código generado tras COMMIT |
| obtenerDoctores | ninguno | List de todos los Doctor, incluidos inactivos |
| buscarDoctorPorCodigo | int código | Doctor o DoctorNoEncontradoException |
| actualizarDoctor | Doctor existente | Doctor actualizado |
| cambiarEstadoDoctor | int código, boolean activo | void; cambio de actividad sin borrar citas |
| consultarDisponibilidad | int doctor, LocalDate fecha, LocalTime hora | boolean; false si inactivo u ocupado; fecha/hora deben ser futuras |
| CitaMedicaServicio.registrarCita | CitaMedica nueva, estado PROGRAMADA | CitaMedica con código generado; exige participantes existentes y activos |
| obtenerCitas | ninguno | List de CitaMedica en todos los estados |
| buscarCitaPorCodigo | int código | CitaMedica o CitaNoEncontradaException |
| obtenerCitasPorPaciente | int paciente | List de CitaMedica; exige existencia de paciente |
| obtenerCitasPorDoctor | int doctor | List de CitaMedica; exige existencia de doctor |
| obtenerCitasPorFecha | LocalDate no nula | List de CitaMedica; admite fechas históricas |
| cancelarCita | int código | CitaMedica Cancelada; exige Programada |
| marcarCitaAtendida | int código | CitaMedica Atendida; exige Programada y horario alcanzado |
| reprogramarCita | int código, LocalDate fecha, LocalTime hora | CitaMedica actualizada; participantes conservados; exige Programada, activos y horario futuro libre |
| consultarDisponibilidad | doctor, fecha, hora | mismo contrato que DoctorServicio |
| validarHorario | fecha, hora | void; valida horario sin consultar MySQL |
| ReporteServicio.obtenerResumen | ninguno | ResumenReporte: totalPacientes, doctoresActivos, citasProgramadas, citasAtendidas, citasCanceladas |
| obtenerDetalleCitas | ninguno | List de DetalleCita: cita, nombrePaciente, nombreDoctor, especialidad; JOIN |
| obtenerCitasPorDoctor / PorPaciente / PorFecha | código o fecha | List de CitaMedica, mismo contrato que servicio de citas |

Todos los servicios pueden lanzar ConexionException o AccesoDatosException. Entradas inválidas generan DatosInvalidosException. Citas con participantes inexistentes generan PacienteNoEncontradoException / DoctorNoEncontradoException. ConflictoHorarioException indica reserva duplicada; OperacionNoPermitidaException indica actividad o transición inválida. Todas derivan de ClinicaException, una RuntimeException. La interfaz decide cómo representar el error; conservar la causa técnica en el registro interno y evitar exponer credenciales o detalles SQL a usuarios.

## Clases y acceso de datos

Persona es abstracta con getters/setters de nombre, teléfono y correo. Paciente agrega getters/setters de código, edad, TipoPaciente y estado; Doctor de código, Especialidad y estado. Sus constructores reciben código, nombre, teléfono y correo, seguidos por los atributos propios en ese orden. CitaMedica recibe código de cita, paciente, doctor, LocalDate, LocalTime, motivo y EstadoCita. Los setters validan nulidad, longitudes, códigos y dominios; el servicio valida existencia y reglas entre registros. Código 0 solo representa objetos aún no registrados. Enums exponen getValor() y desdeValor(String) para conversión SQL; un valor desconocido genera IllegalArgumentException. Los records de reporte exponen accesores con el nombre de sus componentes.

ConexionBD() crea el proveedor de conexiones; abrirConexion() carga com.mysql.jdbc.Driver y conecta a jdbc:mysql://localhost/clinica_universitaria con root y contraseña vacía. Retorna Connection y quien la recibe debe cerrarla. Fallos de carga del driver o de conexión conservan la causa en ConexionException. Los DAO están destinados al servicio: usarlos directamente omite reglas de negocio. Todos reciben Connection y lanzan SQLException; no cierran esa Connection. PacienteDAO y DoctorDAO exponen insertar(Connection, modelo) → código, buscar(Connection, código, bloquear) → Optional, listar(Connection) → List y actualizar(Connection, modelo) → void. CitaMedicaDAO agrega porPaciente, porDoctor, porFecha, disponible(Connection, doctor, fecha, hora, excluirCodigo) y detalles(Connection). buscar con bloquear=true utiliza FOR UPDATE y requiere transacción. mapear(ResultSet) convierte la fila actual al modelo. SQL contiene utilidades internas de PreparedStatement; no forma parte del contrato de la interfaz.

Si se elige escritorio Java, llamar directamente estos servicios desde la capa de aplicación, sin bloquear el hilo gráfico con JDBC. Si se elige web, desarrollar posteriormente una capa HTTP que traduzca entradas y respuestas hacia los servicios. Esta entrega no incluye dicha capa, autenticación, ni interfaz.
