FROM openjdk:21-jdk
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw .

RUN chmod +x mvnw

COPY pom.xml .
COPY src/main ./src/main


RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/api-gateway-0.0.1-SNAPSHOT.jar"]