FROM openjdk:21-jdk
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw .

RUN chmod +x mvnw

COPY pom.xml .

COPY src ./src

RUN ./mvnw package -DskipTests

EXPOSE 8761

CMD ["java", "-jar", "target/service-registry-0.0.1-SNAPSHOT.jar"]