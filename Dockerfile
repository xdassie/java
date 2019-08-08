#FROM maven:3.6.0-jdk-8-alpine
FROM xdassie/java-base:latest
WORKDIR /tmp
COPY pom.xml .
COPY src ./src/
RUN mvn compile
COPY testng.xml ./testng.xml
RUN mvn test 
#|| find ./ && cat  /tmp/target/surefire-reports/*.xml

