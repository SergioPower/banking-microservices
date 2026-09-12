# notification/Dockerfile

# ============================================
# ETAPA 1: Compilación con Maven 3.9
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiar pom.xml primero (cachea dependencias)
COPY pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar (Spring Boot 4.0.x es compatible con Maven 3.6.3+) [citation:4]
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests

# ============================================
# ETAPA 2: Imagen final (solo JRE)
# ============================================
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache curl bash tzdata && \
    rm -rf /var/cache/apk/*

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

RUN addgroup -g 1000 -S appgroup && \
    adduser -u 1000 -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport"

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]