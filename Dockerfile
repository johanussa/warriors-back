# Etapa de construcción con Java 17 y Maven Wrapper
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

# Copiamos el código fuente al contenedor
COPY . .

# Damos permisos al Maven Wrapper (por si hace falta)
RUN chmod +x mvnw

# Empaquetamos la aplicación Quarkus
RUN ./mvnw package -DskipTests

# Etapa de ejecución (imagen liviana solo con JRE)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos los archivos generados por Quarkus en la etapa de build
COPY --from=build /app/target/quarkus-app/ /app/

# Puerto que expondrá el contenedor
EXPOSE 8080

# Comando para iniciar la app
CMD ["java", "-jar", "/app/quarkus-run.jar"]
