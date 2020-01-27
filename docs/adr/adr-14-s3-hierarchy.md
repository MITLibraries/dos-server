# S3 Hierarchy

## Context

Some organization is required for objects as they are ingested in S3.

## Decision

Collection objects will be ingest into a general `collections` folder and then into a collection folder labeled with a prefix for the metadata system of record and the collection identifier from that system. For example, if the collection is described in ArchivesSpace under `/repositories/2/resources/123`, the collection folder would be `collections/as123`. This folder not intended to change if the object moves to another collection in the metadata system of record, it is only intended to organize the items during ingest.

Any folder created by tests will go into a general `tests` folder.

## Status

Pending

## Consequences
This will impact the URLs for these objects which include a universally unique identifier (UUID) for the object. This should not have any impact on the end-user experience.
