FROM maven:3.6.0-jdk-8-alpine
WORKDIR /tmp
COPY pom.xml .
COPY src ./src/
RUN mvn compile
RUN mvn test || find ./ && cat  /tmp/target/surefire-reports/*

