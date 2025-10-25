FROM eclipse-temurin:21-jre
WORKDIR /app
ARG JAR=./fleetwave-web/target/fleetwave-web-0.1.0-SNAPSHOT.jar
COPY ${JAR} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
