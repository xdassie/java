FROM maven:3.5
WORKDIR /tmp
COPY pom.xml .
RUN mvn package
RUN find ./
