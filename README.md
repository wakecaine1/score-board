# score-board
Live Football World Cup Score Board

## About
It is simple implementation of "library" to show matches and scores.

## Tech used
* Java 21 (could be lower version ex. 17)
* Maven
* Spring Context + Spring Test
* JUnit 5
* Mockito
* AssertJ
* Lombok (provided)

All versions can be seen in pom.xml

## Things to mention
Few things to keep in mind:
* this library should be used by some microservice
* **InMemoryMatchRepository** exists in place of actual db repository and can be supplemented with different one
* similarly we can add new services that could serve different types of sports
* tests for service divided into two test files one for happy tests other for failing tests
* added additional test to check for the autowiring spring test
* this will work only if the core project that would use library has spring
* there is additional optional simple micrometer registry configuration added to enrich testing and allow for further implementation of libraries like ex. Prometheus 
