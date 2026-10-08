# Clínica Universitaria Vida UDB

Backend académico Java/JDBC para pacientes, doctores, citas y reportes. Independiente de cualquier interfaz. Sin controladores HTTP, framework ORM ni menú de consola.

## Qué necesitas instalar

1. **JDK 17 o superior** para disponer de `javac` (compilar) y `java` (ejecutar). En este equipo ya están disponibles. Comprobar con `java -version` y `javac -version`.
2. **MySQL Community Server**, mínimo 8.0.16. Para una instalación nueva se recomienda la rama **8.4 LTS** con instalador compatible con tu macOS y arquitectura. Este es el programa que almacena los datos y ejecuta SQL; debe estar iniciado para que Java pueda conectarse. Descargar desde [MySQL Community Server](https://dev.mysql.com/downloads/mysql/). En este Mac con Apple Silicon, elegir macOS ARM64 y el instalador DMG disponible para la versión elegida; ver [instalación oficial para macOS](https://dev.mysql.com/doc/refman/8.4/en/macos-installation-pkg.html).
3. **MySQL Workbench**, opcional pero útil para ejecutar los scripts y consultar las tablas visualmente. Descargar desde [MySQL Workbench](https://dev.mysql.com/downloads/workbench/). Workbench es un cliente: instalarlo por sí solo no instala el servidor. También puedes usar el cliente `mysql` de terminal.
4. **MySQL Connector/J 9.1.0**, driver que permite a Java conectarse con MySQL. El archivo `lib/mysql-connector-j-9.1.0.jar` ya está incluido; no tienes que instalar otro programa. Para recuperarlo manualmente, ver `lib/README.md`.

No se necesita un gestor de compilación ni frameworks adicionales. El proyecto es Java tradicional y se compila con el JDK.

## Preparar MySQL y la base de datos

Después de instalar Community Server, iniciar el servicio MySQL. En Workbench crear una conexión Standard TCP/IP con host `127.0.0.1`, puerto `3306` y usuario `root`; introducir la contraseña que hayas definido en la instalación. En esa conexión abrir y ejecutar:

1. `database/clinica_universitaria.sql` para crear la base y tablas.
2. Opcionalmente, `database/datos_prueba.sql` una sola vez sobre la base recién creada.

No repetir los scripts sobre tablas existentes. Los datos de ejemplo contienen 5 pacientes, 4 doctores y 8 citas: 5 Programada, 2 Atendida y 1 Cancelada. En una base vacía los totales son cero.

Alternativa de terminal para una instalación mediante el DMG oficial de macOS:

```sh
cd "/Users/mac/Documents/Sistema clinico/clinica-universitaria"
/usr/local/mysql/bin/mysql -h 127.0.0.1 -P 3306 -u root -p < database/clinica_universitaria.sql
# Opcional, una sola vez:
/usr/local/mysql/bin/mysql -h 127.0.0.1 -P 3306 -u root -p < database/datos_prueba.sql
```

`-p` solicita la contraseña sin escribirla en el comando. Si root tiene contraseña vacía, presionar Enter. Si el cliente está en otra ubicación, usar su ruta o el comando `mysql` si está en PATH.

## Conexión JDBC conservada

`src/main/java/com/clinica/conexion/ConexionBD.java` conserva exactamente la conexión solicitada:

```java
Class.forName("com.mysql.jdbc.Driver");
Connection conexion = DriverManager.getConnection(
    "jdbc:mysql://localhost/clinica_universitaria", "root", ""
);
```

MySQL debe escuchar en localhost:3306 y permitir las credenciales indicadas. Si al instalar definiste una contraseña para root, colocar esa misma contraseña en el tercer argumento de `getConnection` y volver a compilar. La conexión actual usa contraseña vacía; instalar MySQL con una contraseña no la cambia automáticamente. Ya no se usa archivo de propiedades. El driver incluido conserva el nombre antiguo por compatibilidad y puede mostrar una advertencia de obsolescencia; [documentación MySQL](https://dev.mysql.com/doc/connector-j/en/connector-j-api-changes.html).

## Compilar y ejecutar Java tradicional (macOS/Linux)

Desde el directorio del proyecto:

```sh
cd "/Users/mac/Documents/Sistema clinico/clinica-universitaria"
mkdir -p out
javac -encoding UTF-8 --release 17 -cp "lib/*" -d out @fuentes.txt
java -cp "out:lib/*" com.clinica.Main
```

Si en este Mac aparece “Unable to locate a Java Runtime”, el shell está usando el lanzador del sistema en lugar del JDK ya instalado. Antes de los comandos anteriores ejecutar:

```sh
export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
```

`fuentes.txt` contiene la lista de las 30 clases actuales. `-d out` coloca los `.class` en esa carpeta conservando los paquetes. `-cp` indica dónde encontrar las clases compiladas y el driver externo; las comillas evitan que el shell expanda `*`. `--release 17` mantiene la compatibilidad con Java 17 o superior. Si agregas nuevas clases, regenerar la lista antes de compilar:

```sh
find src/main/java -name '*.java' > fuentes.txt
```

En Windows, crear `out` con `mkdir out`, usar el mismo comando `javac` y cambiar el separador del classpath al ejecutar:

```bat
java -cp "out;lib/*" com.clinica.Main
```

Al abrir la carpeta `clinica-universitaria` en Visual Studio Code, `.vscode/settings.json` configura fuentes, salida `out` y JAR externo en Visual Studio Code. En otro IDE, marcar `src/main/java` como carpeta de fuentes y agregar `lib/mysql-connector-j-9.1.0.jar` como biblioteca externa. Ejecutar la clase `com.clinica.Main`.

`Main` ya tiene un `public static void main(String[] args)` y comprueba conexión, reportes y listados reales. Se conserva sin modificarlo: no crea ni elimina registros y termina al finalizar sus consultas. No quedan pruebas JUnit que reemplazar, porque se eliminaron anteriormente. Una ejecución exitosa con los datos de ejemplo muestra los totales 5, 4, 5, 2 y 1. Esta ejecución de consultas no comprueba por sí sola todas las operaciones de escritura ni la concurrencia.

## Qué era target y qué es out

`target` era la carpeta generada por la herramienta de compilación anterior: guardaba clases `.class`, el JAR generado y otros archivos temporales. No contenía la base MySQL ni el código fuente original. Se eliminó junto con esa configuración.

Ahora `out` cumple el propósito de guardar las clases compiladas con `javac`. Es una carpeta generada que puedes borrar y volver a crear compilando. `src` contiene el código fuente, `database` los scripts SQL y `lib` el driver JDBC manual.

## Organización

- `database/`: estructura y datos de ejemplo separados.
- `src/main/java/com/clinica/modelo/`: Persona abstracta, Paciente y Doctor derivados, CitaMedica y objetos de reporte. Atributos privados, validación en setters y constructores.
- `enums/`: valores Java y equivalentes SQL explícitos.
- `conexion/`: carga explícita del driver y apertura JDBC.
- `dao/`: PreparedStatement, mapeo ResultSet, JOIN y operaciones SQL; recibe una conexión cuya propiedad corresponde al servicio.
- `servicio/`: validaciones, existencia, actividad, disponibilidad y transacciones. Retorna datos o lanza excepciones, sin imprimir ni pedir entradas.
- `excepciones/`: errores de conexión, SQL, datos, inexistencia, horarios y operaciones.
- `Main.java`: ejecución directa de consultas y listados.
- `lib/`: driver JDBC manual y su licencia.
- `fuentes.txt`: lista de archivos Java para compilar.
- `out/`: clases generadas por javac, ignoradas en el control de versiones.
- `INTEGRACION.md`: contrato y ejemplos para los compañeros.

## Integridad y concurrencia

InnoDB, PK autoincrementales, FK con RESTRICT e índices por doctor/horario, paciente/fecha, fecha y estado. No se permite borrar mediante los servicios. Correo y teléfono no son identificadores únicos: personas distintas pueden compartir contacto. El código asignado identifica cada persona; no se intenta inferir identidad por nombre. Sin un documento de identidad en el alcance, no se puede impedir automáticamente registrar dos veces a la misma persona real.

La única columna técnica es `reserva_programada`: vale 1 en Programada y NULL en otros estados. El índice UNIQUE `(codigo_doctor,fecha_cita,hora_cita,reserva_programada)` impide duplicados incluso con SQL directo o transacciones simultáneas. MySQL admite varios NULL en índices únicos, por lo que cancelar libera la reserva sin borrar la cita. Atender también libera la reserva; el servicio impide programar en el pasado. Referencias: [columnas generadas](https://dev.mysql.com/doc/refman/8.0/en/create-table-generated-columns.html) e [índices](https://dev.mysql.com/doc/refman/8.0/en/create-index.html).

La creación bloquea paciente y doctor con SELECT FOR UPDATE y valida actividad en la misma transacción. Actualizaciones y cambios de actividad bloquean esas mismas filas. El índice único es la garantía final de horario, no la consulta previa de disponibilidad. Reprogramación y cambios de estado bloquean la cita. COMMIT confirma; cualquier error provoca ROLLBACK, preservando causas y errores de rollback como suprimidos. Cada operación cierra conexión, statement y ResultSet. No hay conexiones globales compartidas.

Consultar disponibilidad es una observación, no una reserva: puede cambiar antes de registrar. Un interbloqueo o timeout de MySQL se propaga como AccesoDatosException con su SQLException original; la interfaz puede reintentar la operación completa cuando MySQL informa rollback por deadlock. No hay reintentos automáticos ni promesa de que ambas operaciones concurrentes tengan éxito.

Reglas: edad 0..120; contactos y nombres obligatorios; códigos positivos salvo 0 para objetos nuevos; hora con precisión de segundos; fecha/hora futura en America/El_Salvador y año 1000..9999. No se inventan horarios laborales, duración de cita ni reglas de simultaneidad para pacientes. Una cita ocupa el instante exacto solicitado. Programada permite Cancelada o Atendida; solo Programada se reprograma. No se permite atender antes del horario. Inactivar personas conserva citas existentes; solo impide nuevas reservas y reprogramaciones. Las reglas temporales, actividad y transiciones son del servicio: SQL directo puede omitirlas; las FK, CHECK y unicidad permanecen protegidas por MySQL.

## Verificación actual

Verificación realizada el 8 de octubre de 2026:

- Las 30 clases Java compilaron correctamente con el `javac` del JDK 21, `--release 17` y el JAR local. No se utilizó ningún gestor de compilación.
- Se ejecutó `com.clinica.Main` directamente con `java -cp "out:lib/*"`. El driver JDBC se cargó correctamente; la conexión falló con `Connection refused` porque no hay un servidor MySQL escuchando en localhost:3306. No se presenta como comprobada una ejecución exitosa contra la base.
- Se compararon las huellas SHA-256 antes y después: las 30 clases Java y los dos scripts SQL están intactos. Se conserva toda la lógica, conexión y validaciones.
- No quedan `pom.xml`, perfiles de compilación anteriores ni pruebas JUnit. `Main` sigue siendo la clase de comprobación tradicional existente.

Para completar la comprobación funcional, instalar e iniciar MySQL, ejecutar los scripts y repetir el comando Java. Con los datos de ejemplo se deben mostrar los totales indicados arriba.

Los scripts de instalación no eliminan tablas existentes y una segunda ejecución informa que ya existen: no son migraciones.
