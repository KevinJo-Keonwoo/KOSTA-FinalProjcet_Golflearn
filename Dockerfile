FROM openjdk:8
ARG WAR_FILE=target/BackResale-0.0.1-SNAPSHOT.war
COPY ${WAR_FILE} resaledocker.war
ENTRYPOINT ["java","-Xms1024m","-Xmx1024m","-jar","/resaledocker.war"]