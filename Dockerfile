#FROM maven:3.6.0-jdk-8-alpine
FROM xdassie/java-base:latest
WORKDIR /tmp
COPY pom.xml .
COPY soccer_data.txt /soccer_data.txt
COPY /soccer_data.expanded.txt //soccer_data.expanded.txt
RUN hexdump -C /soccer_data.txt
COPY src ./src/
#RUN mvn compile
COPY testng.xml ./testng.xml
ENV MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128m"
RUN mvn test 
#|| find ./ && cat  /tmp/target/surefire-reports/*.xml

