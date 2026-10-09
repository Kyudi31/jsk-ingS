# =======================================================
# Etapa 1: Compilación del proyecto con Maven y Java 17
# =======================================================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copiar descriptor de dependencias para aprovechar la caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copiar el código fuente del proyecto
COPY src ./src

# Empaquetar la aplicación en un archivo JAR ejecutable
RUN mvn clean package -DskipTests -B

# =======================================================
# Etapa 2: Imagen ligera de ejecución (JRE 17)
# =======================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario y grupo de sistema sin privilegios de root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Crear directorio para la base de datos H2 u otros archivos persistentes
RUN mkdir -p /app/data && chown -R spring:spring /app

# Copiar el artefacto generado desde la etapa de compilación
COPY --from=builder --chown=spring:spring /build/target/jsk-*.jar app.jar

# Usar el usuario sin privilegios
USER spring:spring

# Exponer el puerto por defecto de la aplicación Spring Boot
EXPOSE 8080

# Configuración recomendada de memoria para contenedores JVM
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Comando de inicio del contenedor
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
