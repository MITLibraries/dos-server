# AWS Fargate for Docker Container Deployment

## Context

Infrastructure is needed to run Docker containers.

## Decision

The app will be containerized via Docker and deployed to Amazon Fargate (until a replacement choice for running
Docker containers is made by the infrastructure team).

## Status

Accepted

## Consequences

If there are any unanticipated and unresolvable
problems with the deployment mechanism, the deployment can be done to an alternative Docker environment as 
determined by the infrastructure team (such as ECS).