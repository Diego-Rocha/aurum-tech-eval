# aurum-tech-eval

Please see the [mission file](docs/mission.pdf)

## Console (Bash)
```bash
# to build
./gradlew build

# to run (opens on http://localhost:8080)
./gradlew bootRun
```
## Checklist

- [ ] Working API
    - [x] FR 1: save publications
    - [x] FR 2: when type is HEARING then save on schedule
        - [x] get date from classifiedDate
        - [x] or parse date from clippingMatter
        - [x] or plus 3 business days from clippingDate
    - [x] FR 3: when is important then create alert
    - [ ] FR 4: delete one/all publications
    - [ ] FR 5: list the publications with pagination
    - [ ] FR 6: find a publication and mark then as viewed
    - [ ] FR 7: list alerts with pagination
    - [ ] FR 8: list commitments with pagination
- [x] Unit Tests
- [ ] Deploy on Goggle App Engine Standard
- [x] API Docs 
- [x] Quality code with Codebeat (GPA > 3) 
  - Currently: [![codebeat badge](https://codebeat.co/badges/3c364388-42ca-40ac-9630-127afe3604c6)](https://codebeat.co/projects/github-com-diego-rocha-aurum-tech-eval-dev)
- [x] A Good Readme 
- [x] Hosted on Github

*Obs: FR = Funcional Requiremen*t

## Project Release
- Start: 2020-06-25
- Deadline: 2020-07-02

## Stack
 - Java
 - Spring Boot
 - Gradle
 - Google Cloud API
 - Lombok