# Dockerfile para Spring Boot
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar el wrapper de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Dar permisos de ejecución al wrapper
RUN chmod +x mvnw

# Descargar dependencias (esto se cachea si el pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Ejecutar la aplicación
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/*.jar", "--spring.profiles.active=prod"]

