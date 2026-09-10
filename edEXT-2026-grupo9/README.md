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
