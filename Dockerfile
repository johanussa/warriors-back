# Etapa de construcción con Java 17 y Maven Wrapper
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

COPY . .
RUN ls -la
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/quarkus-app/ /app/
RUN chmod +x /app/application

EXPOSE 8080
CMD ["./application"]
