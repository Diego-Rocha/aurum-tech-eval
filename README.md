# aurum-tech-eval

This project is mandatory as part of Aurum's recruitment and selection policy.

[Original requirement](./docs/mission.pdf)

## Console (Bash)
```bash
# Define your GCP Credentials (REQUIRED)
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/your/gcp/credentials.json

# Build the app
> ./gradlew build

# Runs the app (avaliable at http://localhost:8080/publication)
> ./gradlew bootRun

# Generates the jar
> ./gradlew bootJar

## EXTRAS ##
# Generates the code coverage report. ouput in:build/reports/jacoco/test/html/index.html
> ./gradlew jacocoTestReport

# Checks if code coverage is > 90% on packages 'controller, service, business and converter'
> ./gradlew jacocoTestCoverageVerification

```
## Checklist

- [x] Working API (**Required**)
    - [x] FR 1: save publications
    - [x] FR 2: when type is HEARING then save on schedule
        - [x] get date from classifiedDate
        - [x] or parse date from clippingMatter
        - [x] or plus 3 business days from clippingDate
    - [x] FR 3: when is important then create alert
    - [x] FR 4: delete one/all publications
    - [x] FR 5: list the publications with pagination
    - [x] FR 6: view a publication and mark then as viewed
    - [x] FR 7: list alerts with pagination
    - [x] FR 8: list commitments with pagination
- [x] Unit Tests (**Required**)
- [ ] Deploy on Goggle App Engine Standard (25pts)
  - @TODO discover how to use
- [x] Quality code with Codebeat (GPA > 3 = 20pts)
  - Currently: [![codebeat badge](https://codebeat.co/badges/3c364388-42ca-40ac-9630-127afe3604c6)](https://codebeat.co/projects/github-com-diego-rocha-aurum-tech-eval-dev)
- [ ] API Docs  (20pts)
    - @TODO organize samples
- [x] A Good Readme and Hosted on Github (10pts)

## Package Structure

inside the main package 'io.diego.aurum.tech.eval' we have:
- controller: spring rest controllers
- service: spring services
- repository: spring repositories
- config: additional spring config
- business: contain the main application logic
- converter: convert logic between entity and dto
- model: simple models like entity, dto and enums

## Project TimeLine
- Start: 2020-06-25
- Deadline: 2020-07-02

## Stack
 - Java
 - Spring Boot
 - Gradle
 - Google Cloud API
 - Lombok