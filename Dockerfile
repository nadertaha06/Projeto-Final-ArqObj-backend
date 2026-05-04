# syntax=docker/dockerfile:1.7

FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY ecommerce/.mvn/ .mvn/
COPY ecommerce/mvnw ecommerce/pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

COPY ecommerce/src ./src
RUN ./mvnw -B -q -DskipTests package \
 && cp target/*.jar app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/app.jar /app/app.jar

ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
