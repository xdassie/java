FROM maven:3.5
WORKDIR /tmp
COPY pom.xml .
COPY src ./src/
RUN mvn compile
RUN find ./
