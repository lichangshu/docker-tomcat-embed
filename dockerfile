FROM maven:3.5.4-jdk-8

ENV TOMCAT_HOME="/opt/tomcat/"
ENV TOMCAT_PORT=8080

EXPOSE 8080

COPY . /opt/tomcat/
WORKDIR /opt/tomcat/

RUN mvn clean install

CMD ["/usr/bin/java", "-jar", "/opt/tomcat/target/tomcat-0.0.1-SNAPSHOT.jar"]

