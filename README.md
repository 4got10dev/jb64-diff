# JB64 Diff
JSON base64 encoded binary data difference checker

## How to use
Project already has maven wrapper (so you don't need to have it installed). 
All other dependencies (including embedded mongodb) will be downloaded by it.

There are 2 maven/spring-boot profiles that forcing application to work in one of following modes:
- **Active Record** "ar" (default one) maintain one H2 database table record for each diff id that contain left and right part
- **Event Sourcing** "es" maintain new request and result for each addPart call as a new record

Execute maven build (It may take a while in the first run):
```sh
$ ./mvnw clean install 
or
$ ./mvnw clean install -Pes 
```

After the message of BUILD SUCCESS, the application is ready to run:
```sh
$ ./mvnw spring-boot:run
or
$ ./mvnw spring-boot:run -Pes 
```

Application will be available on http://localhost:8787 (This one can be also changed in `application.properties`).
REST API documentation will be available at http://localhost:8787/swagger-ui.html


## Tech stack
- Java 8
- Spring Boot
    - Web
    - Data
	- Data MongoDB
	- Test
- Storage engine
	- H2 database
	- Embedded MongoDB
- Lombok
- SpringFox Swagger UI
- Maven

## Possible improvements
- [ ] More advanced validations (like validation encoded content is it could be decoded)
- [ ] Probably event sourcing document entities could be renamed in order to be self-describing 
- [ ] Use Docker/Docker Compose that will help using installed MongoDB and MySQL
- [ ] Use tool like Gatling to measure performance
