FROM amazoncorretto:21-alpine

# Etiquetas para identificación
LABEL maintainer="banking-platform"
LABEL description="Transfer Service for Banking Platform"
LABEL version="1.0.0"

# Instalar herramientas útiles
RUN apk add --no-cache \
    curl \
    bash \
    && rm -rf /var/cache/apk/*

# Establecer variables de entorno de Java
ENV JAVA_HOME=/usr/lib/jvm/default-jvm
ENV PATH=$JAVA_HOME/bin:$PATH

# Verificar la versión de Java (para debugging)
RUN java -version

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR
COPY target/*.jar app.jar

# Crear usuario no root para seguridad
RUN addgroup -g 1000 -S appgroup && \
    adduser -u 1000 -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

# Cambiar al usuario no root
USER appuser

# Exponer el puerto
EXPOSE 8080

# Configurar opciones de JVM para optimización
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport"

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]