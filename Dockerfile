# =========================================================
# STAGE 1: Compilación automática de la app usando Maven
# =========================================================
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiamos el código fuente al contenedor de compilación
COPY . .

# Compilamos el proyecto saltándonos los tests para ir más rápido
RUN mvn clean package -DskipTests

# =========================================================
# STAGE 2: Imagen de ejecución liviana y segura (Rúbrica)
# =========================================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos únicamente el .jar generado en el Stage 1
COPY --from=build /app/target/*.jar app.jar

# Requisito explícito: Ejecutar bajo usuario seguro no-root (nobody)
USER nobody

# Documentamos el puerto en el que corre Spring Boot
EXPOSE 8080

# Comando para ejecutar la API nativa en la nube
ENTRYPOINT ["java", "-jar", "app.jar"]