# DOS Service

[![Build Status](https://travis-ci.org/MITLibraries/dos-core.svg?branch=master)](https://travis-ci.org/MITLibraries/dos-core)

Service
-----------------------

This documentation and code is a work in progress!

Software Requirements
----------------------

Docker or Java/Maven (for building from source) are required. 
Steps for installation/testing with Docker or Apache Maven are described below.


Building and Running the Artifact from Source
---------------------------------------------

```sh

# from the folder, run the build, and package it:

mvn clean verify

# To run integration tests:

mvn clean verify -P failsafe

# after building, you can run it live by simply running:

java -jar target/dos-server-1.0-SNAPSHOT.war

```

This will start an embedded Tomcat on port 8080.

You can now test the REST API:

```sh
curl -XGET http://localhost:8080/handle?objectId=344

```

Running with Docker
-----------------------

For convenience, the project can be launched with Docker

```sh
# first time only:
docker pull maven:3.5.3-jdk-8-alpine

# from the folder, build the docker image (it will take a while the first time):

docker build -t dos:1.0 .

# now run it:

docker run -p 8080:8080 dos:1.0

# the app should now be live at localhost:8080/dos
 
```


Configuration for Deployments
-------------------------------
Note: Currently, the artifact is manually deployed. This will change.

Adjust application.properties to:

- point to the correct share directory on the server.
- change testing to false


Build using Maven and copy the result .war file to the Tomcat instance.


```sh

# from the folder, run the build, and package it:

mvn clean package -P dev

# scp target/att-0.0.1-SNAPSHOT.war user@server:/usr/share/tomcat/webapps

```

Replace user, server, and tomcat webapp locations, as appropriate.

Tomcat should load the web app after a few moments. Browse to the webapp (specified below)
to confirm the application is working.

To debug, you can tail the following files:

``` sh
tail -f /usr/share/tomcat/tmp/dos-logs/debug.log
sudo tail -f /usr/share/tomcat/logs/localhost.yyyy-mm-dd.log
```

Change Tomcat location, as necessary.

The application logs folder can be changed in application.properties.

Unit Tests
-----------

To run a *single* test:

```sh 
mvn surefire:test -Dtest=edu.mit.dos.handle.HandleServiceTest#testGet

```

Production Server
---------------------

TBD


Authors
-------------

Osman Din and Eric Hanson. 

If you encounter an issue, please file a bug on GitHub (or MIT's JIRA website, 
if you are an internal user).



