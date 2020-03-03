From tomcat:9.0.30-jdk11-openjdk
RUN rm -rf /usr/local/tomcat/webapps/*
RUN apt-get update
RUN apt-get install -y maven
ADD ./ /dos/
WORKDIR /dos
RUN ["mvn", "package", "-DskipTests"]
RUN cp target/dos-server-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
WORKDIR /usr/local/tomcat
CMD ["catalina.sh","run"]