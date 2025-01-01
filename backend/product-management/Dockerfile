FROM openjdk:21-jdk
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw .

RUN chmod +x mvnw

COPY pom.xml .

COPY src/main ./src/main


RUN ./mvnw package -DskipTests

EXPOSE 8082

CMD ["java", "-jar", "target/product-management-0.0.1-SNAPSHOT.jar"]