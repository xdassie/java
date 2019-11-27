FROM maven:3.6.0-jdk-11-slim
WORKDIR /tmp
COPY pom.xml .
COPY src ./src/
RUN mvn compile
COPY testng.xml ./testng.xml
RUN mvn test  
RUN mvn install


