FROM maven:3.5
COPY pom.xml .
RUN mvn package
RUN find ./
