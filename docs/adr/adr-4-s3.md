# Amazon Simple Storage Service (S3)

## Context

Amazon Simple Storage Service (S3) is a robust, scalable, and cost-effective object storage solution. 
It is already being used for various DLS projects and supported by the infrastructure team.

## Decision

AWS S3 will be used for storing bitstreams of digital objects.

## Status

Accepted

## Consequences

We will likely need to coordinate resource planning with the infrastructure team
as the number of digital files DOS needs to store in S3 is currently not known. (It may
also be possible to "reuse S3" links, where feasible.)