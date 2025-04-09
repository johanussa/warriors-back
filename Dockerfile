# Etapa 1: Build del proyecto
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiamos el contenido del proyecto
COPY . .

# Compilamos el proyecto sin ejecutar tests
RUN mvn package -DskipTests

# Etapa 2: Imagen liviana para producción
FROM eclipse-temurin:17-jdk-alpine AS runtime

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en' \
    JAVA_HOME=/usr/lib/jvm/java-17-openjdk/ \
    PATH=$PATH:$JAVA_HOME/bin

WORKDIR /deployments

# Copiamos lo necesario desde la etapa de build
COPY --from=build /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /app/target/quarkus-app/app/ /deployments/app/
COPY --from=build /app/target/quarkus-app/quarkus/ /deployments/quarkus/
COPY --from=build /app/target/quarkus-app/*.jar /deployments/

# Puerto expuesto por la app
EXPOSE 8080

# Variables de entorno Java
ENV JAVA_K8S=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 "
ENV JAVA_SEC=" -Djava.security.egd=file:/dev/./urandom "
ENV JAVA_LOG=" -Dquarkus.http.host=0.0.0.0 -Duser.timezone=America/Bogota -Djava.util.logging.manager=org.jboss.logmanager.LogManager "

# Comando de ejecución
ENTRYPOINT [ "sh", "-c", "java $JAVA_LOG $JAVA_K8S $JAVA_SEC -jar /deployments/quarkus-run.jar" ]
