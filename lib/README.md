# Driver JDBC manual

`mysql-connector-j-9.1.0.jar` está incluido en esta carpeta y se agrega al classpath mediante `-cp "lib/*"`. No requiere instalación ni un gestor de dependencias. Se utiliza JDBC tradicional, sin X DevAPI.

Para recuperar el mismo archivo, descargar Connector/J **9.1.0**, plataforma **Platform Independent**, desde los [archivos oficiales de MySQL](https://downloads.mysql.com/archives/c-j/). Extraer el ZIP o TAR y copiar su archivo `.jar` aquí. Mantener un solo JAR de Connector/J para evitar versiones duplicadas.

La licencia original del archivo se conserva en `LICENSE-Connector-J.txt`. El proyecto mantiene `Class.forName("com.mysql.jdbc.Driver")`; esta versión contiene la clase de compatibilidad y puede mostrar una advertencia de obsolescencia.
