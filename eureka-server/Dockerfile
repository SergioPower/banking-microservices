FROM amazoncorretto:21-alpine

LABEL maintainer="banking-platform"
LABEL description="Eureka Server for Banking Microservices"
LABEL version="1.0.0"

RUN apk add --no-cache curl bash && rm -rf /var/cache/apk/*

WORKDIR /app

COPY target/*.jar app.jar

RUN addgroup -g 1000 -S appgroup && \
    adduser -u 1000 -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

EXPOSE 8761

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]