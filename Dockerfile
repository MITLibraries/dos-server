From tomcat:9.0.30-jdk11-openjdk
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./target/dos-server-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/dos-server.war
CMD ["catalina.sh","run"]