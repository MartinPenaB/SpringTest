FROM eclipse-temurin:23-jdk-alpine AS build
COPY . .

FROM eclipse-temurin:23
COPY --from=build /target/japnet-0.0.1-SNAPSHOT.jar japnet.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "japnet.jar" ]