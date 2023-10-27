FROM maven:3.9.5 as build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/backend.jar .
CMD ["java", "-jar", "backend.jar"]
