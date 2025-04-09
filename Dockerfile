# Base image con Java 17 ya instalado
FROM eclipse-temurin:17-jdk-alpine AS base

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en' \
    JAVA_HOME=/usr/lib/jvm/java-17-openjdk/ \
    PATH=$PATH:$JAVA_HOME/bin

LABEL maintainer="sodimac@vc-soft.com"

# Carpeta de trabajo dentro del contenedor
WORKDIR /deployments

# Copiamos las carpetas generadas por Quarkus
COPY target/quarkus-app/lib/ /deployments/lib/
COPY target/quarkus-app/app/ /deployments/app/
COPY target/quarkus-app/quarkus/ /deployments/quarkus/
COPY target/quarkus-app/*.jar /deployments/

# Exponemos el puerto del servicio (puedes fijarlo si gustas)
EXPOSE 8080

# Variables de entorno Java
ENV JAVA_K8S=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 "
ENV JAVA_SEC=" -Djava.security.egd=file:/dev/./urandom "
ENV JAVA_LOG=" -Dquarkus.http.host=0.0.0.0 -Duser.timezone=America/Bogota -Djava.util.logging.manager=org.jboss.logmanager.LogManager "

# Comando de inicio del microservicio
ENTRYPOINT [ "sh", "-c", "java $JAVA_LOG $JAVA_K8S $JAVA_SEC -jar /deployments/quarkus-run.jar" ]
