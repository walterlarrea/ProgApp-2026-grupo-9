# edEXT 2026 - Grupo 9

## Requisitos

- Java 21
- MySQL Server
- Una base de datos llamada `edEXT`

## Crear el JAR de lanzamiento

Desde la raíz del repositorio, ejecutar:

```powershell
cd edEXT-2026-grupo9
mvn clean package -DskipTests
```

El JAR ejecutable se genera en:

```text
target/edEXT-2026-grupo9-1.0-SNAPSHOT.jar
```

El JAR incluye las dependencias de la aplicación, incluyendo Hibernate, JPA, MySQL Connector/J y dotenv-java.

## Configurar la base de datos

Copiar `.env.example` al directorio de lanzamiento con el nombre `.env` y reemplazar los valores de ejemplo:

```powershell
copy .env.example target\.env
```

El archivo `.env` debe contener:

```env
ED_EXT_DB_URL=jdbc:mysql://localhost:3306/edEXT?serverTimezone=UTC
ED_EXT_DB_USER=db-username
ED_EXT_DB_PASSWORD=db-password
```

Mantener `.env` en el mismo directorio desde el que se ejecuta el JAR. No subir `.env` al repositorio, ya que puede contener la contraseña de la base de datos.

## Ejecutar el lanzamiento

Desde el directorio del proyecto, ejecutar:

```powershell
cd target
java -jar edEXT-2026-grupo9-1.0-SNAPSHOT.jar
```

Para entregar el programa al cliente, copiar el JAR y el archivo `.env` configurado en la misma carpeta de su equipo. El cliente debe tener Java 21, MySQL Server y acceso a la base de datos configurada.


---

## Despliegue Distribuido en Diferentes Máquinas (Sin NetBeans)

Esta sección describe cómo desplegar el **Servidor Central** (con Web Services SOAP) y el **Servidor Web** (Tomcat + Servlets/JSP) en computadoras distintas utilizando el archivo de configuración `config.properties`.

### 1. Compilación y Generación de Entregables (Máquina de Desarrollo)

No es necesario tener NetBeans ni Maven en las computadoras de los clientes. Los ejecutables se empaquetan una sola vez desde la terminal del desarrollador:

```powershell
cd edEXT-2026-grupo9
mvn clean package -DskipTests
```

Esto generará los siguientes archivos:
* **Servidor Central (Fat JAR):**
  `servidor-central/target/servidor-central-1.0-SNAPSHOT.jar`
* **Servidor Web (WAR):**
  `servidor-web/target/servidor-web-1.0-SNAPSHOT.war`

---

### 2. Despliegue de la Máquina A (Servidor Central)

> **Requisitos:** Java 21 (JRE o JDK) y MySQL Server.

1. **Base de Datos:**
   - Asegurarse de que el servicio de MySQL esté corriendo.
   - Crear la base de datos `edEXT` y cargar los datos iniciales con `populate_edext.sql`.
2. **Preparar carpeta de despliegue:**
   - Copiar `servidor-central-1.0-SNAPSHOT.jar` a una carpeta de la máquina (por ejemplo `C:\edext-central\`).
   - Crear en la misma carpeta el archivo `config.properties` (o basarse en `config.properties.example`):
     ```properties
     # Base de datos MySQL
     db.url=jdbc:mysql://localhost:3306/edEXT?serverTimezone=UTC
     db.user=root
     db.password=tu_password

     # Publicacion SOAP: 0.0.0.0 permite recibir conexiones desde otras maquinas de la red
     ws.host=http://0.0.0.0
     ws.port=9128
     ```
3. **Firewall:**
   - Habilitar una regla de entrada en el Firewall para el puerto **9128 TCP** para permitir que el Servidor Web se conecte.
4. **Obtener la IP de la Máquina A:**
   - Ejecutar `ipconfig` (Windows) o `ip a` (Linux) y anotar la dirección IP local (ejemplo: `192.168.1.50`).
5. **Ejecutar el Servidor Central:**
   ```powershell
   java -jar servidor-central-1.0-SNAPSHOT.jar
   ```
   Verás en consola la confirmación de la publicación de cada servicio SOAP.

---

### 3. Despliegue de la Máquina B (Servidor Web)

> **Requisitos:** Java 21 y Apache Tomcat 10.1.x (Importante: debe ser Tomcat 10.1 por compatibilidad con Jakarta EE 10 / Servlet 6.0).

1. **Configurar la conexión hacia el Servidor Central:**
   - Crear la carpeta `.edext` dentro del directorio home del usuario de la máquina (por ejemplo `C:\Users\<TuUsuario>\.edext\` en Windows o `/home/<usuario>/.edext/` en Linux).
   - Crear dentro el archivo `config.properties`:
     ```properties
     # IP y puerto de la Maquina A donde esta corriendo el Servidor Central
     ws.central.host=192.168.1.50
     ws.central.port=9128
     ```
     *(Reemplazar `192.168.1.50` por la IP real de la Máquina A)*.
2. **Instalar la aplicación en Tomcat:**
   - Descargar y descomprimir Apache Tomcat 10.1 en la máquina.
   - Copiar el archivo `servidor-web-1.0-SNAPSHOT.war` dentro de la carpeta `webapps/` de Tomcat.
   - *(Opcional)* Renombrar el archivo a `edext.war` o a `ROOT.war` (borrando previamente la carpeta `ROOT` existente) para una URL más simple.
3. **Iniciar Tomcat:**
   - Ir a la carpeta `bin/` de Tomcat y ejecutar `startup.bat` (Windows) o `./startup.sh` (Linux).
4. **Acceso al sistema:**
   - Abrir un navegador web e ingresar a:
     `http://localhost:8080/servidor-web-1.0-SNAPSHOT/` (o `http://localhost:8080/edext/`).
   - El Servidor Web consumirá las funciones del Servidor Central a través de la red automáticamente.
