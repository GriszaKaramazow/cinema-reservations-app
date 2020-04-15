# cinema-reservations-app

The REST app simulates cinema reservation system.

### Requirements

The app requires:

- Java Development Kit
- PostgreSQL
- Maven 

An app's setup requires `cinema` account in the database with password `cinema`. 
Alternatively, the setup can be changed in file `application.properties` in `src/main/resources/` 

### Endpoints

#### `/signup`

- register `CLIENT`'s account via `POST` request
- accessible by `ANONYMOUS`

#### `/register`

- register `EMPLOYEE`'s account via `POST` request
- accessible only by users logged as `EMPLOYEE`

#### `/login`

- allows to log in to the system as `CLIENT` or `EMPLOYEE` via `POST` request

#### `/myaccount`

- allows to obtain user's data
- accessible by `CLIENT` and `EMPLOYEE` via `GET` request

#### `/movies`

- allows to obtain list of movies from the database via `GET` request
- allows query by example requests via request parameters (`id`, `title`, `category`, `length` and `ageLimit`)
- accessible by `ANONYMOUS`, `CLIENT` and `EMPLOYEE`
- `EMPLOYEE`s are allowed to update and delete entities via `PUT` and `DELETE` requests

#### `/room`

- allows to obtain list of movies from the database via `GET` request
- allows query by example requests via request parameters (`id` and `capacity`)
- accessible only by `EMPLOYEE`s
- allows to update and delete entities via `PUT` and `DELETE` requests

#### `/session`

- allows to obtain a list of movie session from the database via `GET` request
- allows query by example requests via request parameters (`movie`, `room`, `date`, `time` and `price`)
- accessible by `ANONYMOUS`, `CLIENT` and `EMPLOYEE`
- `EMPLOYEE`s are allowed to update and delete entities via `PUT` and `DELETE` requests

#### `/session/seats`

- allows to obtain a list of seats for appropriate session from the database via `GET` request
- the request requires `id` of the session as the parameter i.e. `/session/seats?id=1`
- accessible only by `CLIENT`s

#### `/tickets`

- allows to obtain a list of tickets from the database via `GET` request
- allows query by example requests via request parameters
- accessible only by `EMPLOYEE`s
- allows to delete entities via `DELETE` requests
- an entity cannot be changed

#### `/mytickets`

- allows to obtain a list of tickets reserved by currently logged `CLIENT` from the database via `GET` request
- allows query by example requests via request parameters
- accessible only by `CLIENT`s

#### `/reservation`

- allows to reserve tickets for the session via `POST` request
- accessible only by `CLIENT`s

Exemplary requests are available in `src/main/resources/CinemaReservationApp-2.5.3.json`. 
The requests can be inquired by Postman. To test the app import the collection and run it.
The requests inherit a security token automatically. 
If you want to re-run tests, it is essential to re-run app (`CTLR` + `F5` in IntelliJ) in order to rebuild the database.

### The app was build with

* [Maven](https://maven.apache.org)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Security](https://spring.io/projects/spring-security)
* [Java JWT](https://github.com/auth0/java-jwt)
* [PostgreSQL](https://www.postgresql.org/)
* [Project Lombok](https://projectlombok.org/)
* [JUnit Jupiter](https://junit.org/junit5)
* [H2 Database](https://www.h2database.com/)

### Developers

* [Mirosław Buszek](https://github.com/MiroslawBuszek)
* [Sebastian Kwaśniewski](https://github.com/kwachu2)
* [Grzegorz Witkowski](https://github.com/GriszaKaramazow)