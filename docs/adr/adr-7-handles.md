# Handle System

## Context

Handles are persistent links that currently point to Dome pages. 
The Handles server is embedded in Dome.

An example in [Dome](https://dome.mit.edu/handle/1721.3/82731). 

As handles point to a web page, and as DOS is not meant to expose a web interface for public consumption,
the handles emitted by Dome can continue to point to Dome and the Handle server can remain embedded in Dome,
until such time when an application other than Dome is in production. 

## Decision

Handles will not be minted by DOS and the Handle server will remain in Dome. 

## Status

Accepted

## Consequences

1. Whenever a new "discovery" or frontend interface is adopted by MIT Libraries (and a business decision
has been made on the use handles), the handles stored by DOS will need to be updated to point to the new
location.

2. DOS will still need to ensure TODO

