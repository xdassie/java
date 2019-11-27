#FROM maven:3.6.0-jdk-8-alpine
FROM maven:3.6.0-jdk-11-slim
WORKDIR /tmp
COPY pom.xml .
COPY src ./src/
#RUN apk update && apk upgrade
RUN apt-get update && apt-get upgrade -y
RUN mvn compile
COPY testng.xml ./testng.xml  
RUN mvn install -DskipTests



