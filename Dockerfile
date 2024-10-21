FROM maven:3.9-openjdk-23 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:23
COPY --from=build /target/japnet-0.0.1-SNAPSHOT.jar japnet.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "japnet.jar" ]