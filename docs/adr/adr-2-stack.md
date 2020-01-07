# Software Stack

## Context

Use a robust software stack (web server, application container, database, programming language, framework)
to realize underlying functionality and fulfill non-functional requirements 
of scalability, performance, reliability, and security, and backward compatibility out 
of the box.


## Decision

The core service will use Java and Spring framework as the basic stack. For ingest component, Python will be used as
the scripting language. Postgres will be used for storing information about the digitial objects.

## Status

Accepted

## Consequences

1. As most DLS engineers are not proficient in Java or JDK languages, training may need to be provided to 
other team members. The project team plans to train additional developers when we have the capacity in DLS to 
ensure operational coverage.

2. We will use an OpenJDK build, as there is a license associated with OracleJDK.