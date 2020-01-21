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

# From the sourcecode folder, run Maven for the build (without integration tests):

mvn clean verify

# (Optional) To include and run the integration tests while building:

mvn clean verify -P failsafe

# Or, to run a single test:

mvn surefire:test -Dtest=edu.mit.dos.handle.HandleServiceTest#testGet

# After building ("BUILD SUCCESS"), you can run the artifact using the java launcher:

java -jar target/dos-server-1.0-SNAPSHOT.war

```

This will start an embedded Tomcat on port 8080. You can now test the REST API:

```sh
curl -XGET http://localhost:8080/object?oid=344

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
Note: For now the artifact will be manually deployed. 

1. Adjust application.properties to pass any required parameters. 

2. Build using Maven and copy the result .war file to the Tomcat instance.

```sh

# from the folder, run the build, and package it:

mvn clean package -P dev

# copy the file to the server, replacing username, server information, and the path to tomcat webapps folder

scp target/dos-server-1.0-SNAPSHOT.war username@server:/path/to/tomcat

```

Tomcat should load the web app after a few moments. 

To debug, you can tail the following files:

``` sh
tail -f /path/to/tomcat/tmp/dos-logs/debug.log
sudo tail -f /path/to/tomcat/logs/localhost.yyyy-mm-dd.log
```

To make DOS post bistreams against S3 (as opposed to local filesystem), 
change spring active profile in application.properties to "stage," and add 
file application-stage.properties to resources folder
(parallel to application.properties) with the following information:

``` sh

security.basic.enable: false
security.ignored=/**

serviceConfig.storage=s3

serviceConfig.accessKey=your_access_key
serviceConfig.secretKey=your_secret_key
serviceConfig.bucket=dos-stage
```

Same applies for running integration tests against S3. Change spring active profiles to
stage, and just copy/paste application-stage.properties
to the relevant test folder (again, parallel to application.properties). 
By default integration tests are run against the local file system.

Deployments 
-----------------------

TBD


Support
-------------------

If you encounter an issue, please file a bug on GitHub (or MIT's JIRA website).

Contributors
-------------

Osman Din & Eric Hanson. 